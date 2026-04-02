package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.MaintenanceRequestResponseDto;
import com.example.maintenancerequests.dto.StatusHistoryResponseDto;
import com.example.maintenancerequests.dto.UpdateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.UpdateRequestStatusDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.StatusHistory;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.enums.Role;
import com.example.maintenancerequests.exception.AuthenticationRequiredException;
import com.example.maintenancerequests.enums.RequestStatus;
import com.example.maintenancerequests.exception.ForbiddenOperationException;
import com.example.maintenancerequests.exception.InvalidRequestAssignmentException;
import com.example.maintenancerequests.exception.InvalidStatusTransitionException;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.MaintenanceRequestMapper;
import com.example.maintenancerequests.mapper.StatusHistoryMapper;
import com.example.maintenancerequests.repository.EquipmentRepository;
import com.example.maintenancerequests.repository.MaintenanceRequestRepository;
import com.example.maintenancerequests.repository.StatusHistoryRepository;
import com.example.maintenancerequests.repository.StoreRepository;
import com.example.maintenancerequests.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceRequestServiceImpl implements MaintenanceRequestService {

    private static final Set<Role> ROLES_ALLOWED_TO_ASSIGN_ENGINEER =
            Set.of(Role.ADMIN, Role.DISPATCHER, Role.MANAGER);

    private static final Map<RequestStatus, Set<RequestStatus>> ALLOWED_STATUS_TRANSITIONS = Map.of(
            RequestStatus.NEW, Set.of(RequestStatus.ASSIGNED, RequestStatus.CANCELLED),
            RequestStatus.ASSIGNED, Set.of(RequestStatus.IN_PROGRESS, RequestStatus.CANCELLED),
            RequestStatus.IN_PROGRESS, Set.of(
                    RequestStatus.WAITING_PARTS,
                    RequestStatus.DONE,
                    RequestStatus.CANCELLED
            ),
            RequestStatus.WAITING_PARTS, Set.of(RequestStatus.IN_PROGRESS, RequestStatus.CANCELLED),
            RequestStatus.DONE, Set.of(),
            RequestStatus.CANCELLED, Set.of()
    );

    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final StoreRepository storeRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final MaintenanceRequestMapper maintenanceRequestMapper;
    private final StatusHistoryMapper statusHistoryMapper;

    @Override
    @Transactional
    public MaintenanceRequestResponseDto createMaintenanceRequest(CreateMaintenanceRequestDto dto) {
        Store store = findStoreByIdOrThrow(dto.getStoreId());
        Equipment equipment = findEquipmentByIdIfProvided(dto.getEquipmentId());
        User createdBy = getCurrentAuthenticatedUser();
        User assignedEngineer = resolveAssignedEngineer(dto.getAssignedEngineerId(), createdBy);

        MaintenanceRequest maintenanceRequest = maintenanceRequestMapper.toEntity(
                dto,
                store,
                equipment,
                createdBy,
                assignedEngineer,
                RequestStatus.NEW
        );

        MaintenanceRequest savedMaintenanceRequest = maintenanceRequestRepository.save(maintenanceRequest);
        return maintenanceRequestMapper.toResponseDto(savedMaintenanceRequest);
    }

    @Override
    public List<MaintenanceRequestResponseDto> getAllMaintenanceRequests(
            RequestStatus status,
            Long storeId,
            Long assignedEngineerId
    ) {
        validateFilterReferences(storeId, assignedEngineerId);

        return maintenanceRequestRepository.findAll()
                .stream()
                .filter(request -> status == null || request.getStatus() == status)
                .filter(request -> storeId == null
                        || (request.getStore() != null && storeId.equals(request.getStore().getId())))
                .filter(request -> assignedEngineerId == null
                        || (request.getAssignedEngineer() != null
                        && assignedEngineerId.equals(request.getAssignedEngineer().getId())))
                .map(maintenanceRequestMapper::toResponseDto)
                .toList();
    }

    @Override
    public MaintenanceRequestResponseDto getMaintenanceRequestById(Long id) {
        MaintenanceRequest maintenanceRequest = findMaintenanceRequestByIdOrThrow(id);
        return maintenanceRequestMapper.toResponseDto(maintenanceRequest);
    }

    @Override
    @Transactional
    public MaintenanceRequestResponseDto updateMaintenanceRequest(Long id, UpdateMaintenanceRequestDto dto) {
        MaintenanceRequest maintenanceRequest = findMaintenanceRequestByIdOrThrow(id);
        Equipment equipment = findEquipmentByIdIfProvided(dto.getEquipmentId());
        User currentUser = getCurrentAuthenticatedUser();
        User assignedEngineer = resolveAssignedEngineerForUpdate(
                maintenanceRequest,
                dto.getAssignedEngineerId(),
                currentUser
        );

        maintenanceRequestMapper.updateEntity(dto, maintenanceRequest, equipment, assignedEngineer);

        MaintenanceRequest updatedMaintenanceRequest = maintenanceRequestRepository.save(maintenanceRequest);
        return maintenanceRequestMapper.toResponseDto(updatedMaintenanceRequest);
    }

    @Override
    @Transactional
    public MaintenanceRequestResponseDto updateRequestStatus(Long id, UpdateRequestStatusDto dto) {
        MaintenanceRequest maintenanceRequest = findMaintenanceRequestByIdOrThrow(id);
        User changedBy = getCurrentAuthenticatedUser();
        RequestStatus oldStatus = maintenanceRequest.getStatus();
        validateStatusChange(maintenanceRequest, dto.getStatus());

        maintenanceRequest.setStatus(dto.getStatus());
        MaintenanceRequest updatedMaintenanceRequest = maintenanceRequestRepository.save(maintenanceRequest);

        StatusHistory statusHistory = StatusHistory.builder()
                .maintenanceRequest(updatedMaintenanceRequest)
                .oldStatus(oldStatus)
                .newStatus(dto.getStatus())
                .changedBy(changedBy)
                .build();
        statusHistoryRepository.save(statusHistory);

        return maintenanceRequestMapper.toResponseDto(updatedMaintenanceRequest);
    }

    @Override
    public List<StatusHistoryResponseDto> getStatusHistoryByRequestId(Long id) {
        findMaintenanceRequestByIdOrThrow(id);

        return statusHistoryRepository.findByMaintenanceRequestIdOrderByChangedAtAsc(id)
                .stream()
                .map(statusHistoryMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteMaintenanceRequest(Long id) {
        MaintenanceRequest maintenanceRequest = findMaintenanceRequestByIdOrThrow(id);
        maintenanceRequestRepository.delete(maintenanceRequest);
    }

    private MaintenanceRequest findMaintenanceRequestByIdOrThrow(Long id) {
        return maintenanceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance request not found with id: " + id));
    }

    private Store findStoreByIdOrThrow(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
    }

    private Equipment findEquipmentByIdIfProvided(Long id) {
        if (id == null) {
            return null;
        }

        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
    }

    private User findUserByIdIfProvided(Long id) {
        if (id == null) {
            return null;
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private User resolveAssignedEngineer(Long assignedEngineerId, User currentUser) {
        if (assignedEngineerId == null) {
            return null;
        }

        validateCurrentUserCanAssignEngineer(currentUser);

        User assignedEngineer = findUserByIdIfProvided(assignedEngineerId);
        validateAssignedEngineerRole(assignedEngineer);

        return assignedEngineer;
    }

    private User resolveAssignedEngineerForUpdate(
            MaintenanceRequest maintenanceRequest,
            Long newAssignedEngineerId,
            User currentUser
    ) {
        Long currentAssignedEngineerId = maintenanceRequest.getAssignedEngineer() != null
                ? maintenanceRequest.getAssignedEngineer().getId()
                : null;

        if ((currentAssignedEngineerId == null && newAssignedEngineerId == null)
                || (currentAssignedEngineerId != null && currentAssignedEngineerId.equals(newAssignedEngineerId))) {
            return maintenanceRequest.getAssignedEngineer();
        }

        if (newAssignedEngineerId == null) {
            validateCurrentUserCanAssignEngineer(currentUser);
            return null;
        }

        return resolveAssignedEngineer(newAssignedEngineerId, currentUser);
    }

    private void validateCurrentUserCanAssignEngineer(User currentUser) {
        if (!ROLES_ALLOWED_TO_ASSIGN_ENGINEER.contains(currentUser.getRole())) {
            throw new ForbiddenOperationException(
                    "Only ADMIN, DISPATCHER, or MANAGER can assign or reassign an engineer"
            );
        }
    }

    private void validateAssignedEngineerRole(User assignedEngineer) {
        if (assignedEngineer != null && assignedEngineer.getRole() != Role.ENGINEER) {
            throw new InvalidRequestAssignmentException("Assigned user must have role ENGINEER");
        }
    }

    private void validateStatusChange(MaintenanceRequest maintenanceRequest, RequestStatus newStatus) {
        RequestStatus currentStatus = maintenanceRequest.getStatus();

        if (currentStatus == newStatus) {
            throw new InvalidStatusTransitionException("New status must be different from current status");
        }

        if (newStatus == RequestStatus.IN_PROGRESS && maintenanceRequest.getAssignedEngineer() == null) {
            throw new InvalidStatusTransitionException(
                    "Request cannot move to IN_PROGRESS without an assigned engineer"
            );
        }

        Set<RequestStatus> allowedStatuses = ALLOWED_STATUS_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowedStatuses.contains(newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found in security context");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException(
                        "Authenticated user not found with email: " + authentication.getName()
                ));
    }

    private void validateFilterReferences(Long storeId, Long assignedEngineerId) {
        if (storeId != null) {
            findStoreByIdOrThrow(storeId);
        }

        if (assignedEngineerId != null) {
            User assignedEngineer = findUserByIdIfProvided(assignedEngineerId);
            validateAssignedEngineerRole(assignedEngineer);
        }
    }
}
