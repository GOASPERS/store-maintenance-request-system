package com.example.maintenancerequests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.maintenancerequests.dto.CreateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.MaintenanceRequestResponseDto;
import com.example.maintenancerequests.dto.UpdateRequestStatusDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.enums.RequestPriority;
import com.example.maintenancerequests.enums.RequestStatus;
import com.example.maintenancerequests.enums.Role;
import com.example.maintenancerequests.exception.InvalidRequestAssignmentException;
import com.example.maintenancerequests.exception.InvalidStatusTransitionException;
import com.example.maintenancerequests.mapper.MaintenanceRequestMapper;
import com.example.maintenancerequests.mapper.StatusHistoryMapper;
import com.example.maintenancerequests.repository.EquipmentRepository;
import com.example.maintenancerequests.repository.MaintenanceRequestRepository;
import com.example.maintenancerequests.repository.StatusHistoryRepository;
import com.example.maintenancerequests.repository.StoreRepository;
import com.example.maintenancerequests.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class MaintenanceRequestServiceImplTest {

    @Mock
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusHistoryRepository statusHistoryRepository;

    @Mock
    private MaintenanceRequestMapper maintenanceRequestMapper;

    @Mock
    private StatusHistoryMapper statusHistoryMapper;

    @InjectMocks
    private MaintenanceRequestServiceImpl maintenanceRequestService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createMaintenanceRequestSuccess() {
        setAuthenticatedUser("manager@demo.local");

        CreateMaintenanceRequestDto dto = new CreateMaintenanceRequestDto(
                "Broken printer",
                "Needs repair",
                RequestPriority.HIGH,
                1L,
                2L,
                3L,
                null
        );

        Store store = Store.builder().id(1L).build();
        Equipment equipment = Equipment.builder().id(2L).build();
        User currentUser = User.builder().id(10L).email("manager@demo.local").role(Role.MANAGER).active(true).build();
        User engineer = User.builder().id(3L).role(Role.ENGINEER).active(true).build();
        MaintenanceRequest maintenanceRequest = MaintenanceRequest.builder().title("Broken printer").build();
        MaintenanceRequest savedRequest = MaintenanceRequest.builder().id(100L).title("Broken printer").build();
        MaintenanceRequestResponseDto responseDto = MaintenanceRequestResponseDto.builder().id(100L).title("Broken printer").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(equipment));
        when(userRepository.findByEmail("manager@demo.local")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(engineer));
        when(maintenanceRequestMapper.toEntity(
                dto,
                store,
                equipment,
                currentUser,
                engineer,
                RequestStatus.NEW
        )).thenReturn(maintenanceRequest);
        when(maintenanceRequestRepository.save(maintenanceRequest)).thenReturn(savedRequest);
        when(maintenanceRequestMapper.toResponseDto(savedRequest)).thenReturn(responseDto);

        MaintenanceRequestResponseDto result = maintenanceRequestService.createMaintenanceRequest(dto);

        assertEquals(100L, result.getId());
        verify(maintenanceRequestRepository).save(maintenanceRequest);
    }

    @Test
    void updateRequestStatusSuccess() {
        setAuthenticatedUser("dispatcher@demo.local");

        User currentUser = User.builder().id(1L).email("dispatcher@demo.local").role(Role.DISPATCHER).active(true).build();
        MaintenanceRequest maintenanceRequest = MaintenanceRequest.builder()
                .id(11L)
                .status(RequestStatus.NEW)
                .build();
        MaintenanceRequest savedRequest = MaintenanceRequest.builder()
                .id(11L)
                .status(RequestStatus.ASSIGNED)
                .build();
        MaintenanceRequestResponseDto responseDto = MaintenanceRequestResponseDto.builder()
                .id(11L)
                .status(RequestStatus.ASSIGNED)
                .build();

        when(maintenanceRequestRepository.findById(11L)).thenReturn(Optional.of(maintenanceRequest));
        when(userRepository.findByEmail("dispatcher@demo.local")).thenReturn(Optional.of(currentUser));
        when(maintenanceRequestRepository.save(maintenanceRequest)).thenReturn(savedRequest);
        when(maintenanceRequestMapper.toResponseDto(savedRequest)).thenReturn(responseDto);

        MaintenanceRequestResponseDto result = maintenanceRequestService.updateRequestStatus(
                11L,
                new UpdateRequestStatusDto(RequestStatus.ASSIGNED)
        );

        assertEquals(RequestStatus.ASSIGNED, result.getStatus());
        verify(statusHistoryRepository).save(any());
    }

    @Test
    void updateRequestStatusSameStatusError() {
        setAuthenticatedUser("dispatcher@demo.local");

        User currentUser = User.builder().email("dispatcher@demo.local").role(Role.DISPATCHER).active(true).build();
        MaintenanceRequest maintenanceRequest = MaintenanceRequest.builder()
                .id(11L)
                .status(RequestStatus.NEW)
                .build();

        when(maintenanceRequestRepository.findById(11L)).thenReturn(Optional.of(maintenanceRequest));
        when(userRepository.findByEmail("dispatcher@demo.local")).thenReturn(Optional.of(currentUser));

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> maintenanceRequestService.updateRequestStatus(11L, new UpdateRequestStatusDto(RequestStatus.NEW))
        );

        verify(statusHistoryRepository, never()).save(any());
    }

    @Test
    void updateRequestStatusToInProgressWithoutAssignedEngineerError() {
        setAuthenticatedUser("dispatcher@demo.local");

        User currentUser = User.builder().email("dispatcher@demo.local").role(Role.DISPATCHER).active(true).build();
        MaintenanceRequest maintenanceRequest = MaintenanceRequest.builder()
                .id(11L)
                .status(RequestStatus.ASSIGNED)
                .assignedEngineer(null)
                .build();

        when(maintenanceRequestRepository.findById(11L)).thenReturn(Optional.of(maintenanceRequest));
        when(userRepository.findByEmail("dispatcher@demo.local")).thenReturn(Optional.of(currentUser));

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> maintenanceRequestService.updateRequestStatus(
                        11L,
                        new UpdateRequestStatusDto(RequestStatus.IN_PROGRESS)
                )
        );

        verify(statusHistoryRepository, never()).save(any());
    }

    @Test
    void assigningNonEngineerUserError() {
        setAuthenticatedUser("manager@demo.local");

        CreateMaintenanceRequestDto dto = new CreateMaintenanceRequestDto(
                "Broken printer",
                "Needs repair",
                RequestPriority.HIGH,
                1L,
                null,
                5L,
                null
        );

        Store store = Store.builder().id(1L).build();
        User currentUser = User.builder().id(10L).email("manager@demo.local").role(Role.MANAGER).active(true).build();
        User nonEngineer = User.builder().id(5L).role(Role.MANAGER).active(true).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(userRepository.findByEmail("manager@demo.local")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(5L)).thenReturn(Optional.of(nonEngineer));

        assertThrows(
                InvalidRequestAssignmentException.class,
                () -> maintenanceRequestService.createMaintenanceRequest(dto)
        );
    }

    private void setAuthenticatedUser(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );
    }
}
