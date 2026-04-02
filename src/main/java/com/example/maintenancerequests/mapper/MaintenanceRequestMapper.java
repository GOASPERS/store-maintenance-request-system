package com.example.maintenancerequests.mapper;

import com.example.maintenancerequests.dto.CreateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.MaintenanceRequestResponseDto;
import com.example.maintenancerequests.dto.UpdateMaintenanceRequestDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.enums.RequestStatus;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceRequestMapper {

    public MaintenanceRequest toEntity(
            CreateMaintenanceRequestDto dto,
            Store store,
            Equipment equipment,
            User createdBy,
            User assignedEngineer,
            RequestStatus status
    ) {
        if (dto == null) {
            return null;
        }

        return MaintenanceRequest.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(status)
                .store(store)
                .equipment(equipment)
                .createdBy(createdBy)
                .assignedEngineer(assignedEngineer)
                .dueDate(dto.getDueDate())
                .build();
    }

    public void updateEntity(
            UpdateMaintenanceRequestDto dto,
            MaintenanceRequest maintenanceRequest,
            Equipment equipment,
            User assignedEngineer
    ) {
        if (dto == null || maintenanceRequest == null) {
            return;
        }

        maintenanceRequest.setTitle(dto.getTitle());
        maintenanceRequest.setDescription(dto.getDescription());
        maintenanceRequest.setPriority(dto.getPriority());
        maintenanceRequest.setEquipment(equipment);
        maintenanceRequest.setAssignedEngineer(assignedEngineer);
        maintenanceRequest.setDueDate(dto.getDueDate());
    }

    public MaintenanceRequestResponseDto toResponseDto(MaintenanceRequest maintenanceRequest) {
        if (maintenanceRequest == null) {
            return null;
        }

        return MaintenanceRequestResponseDto.builder()
                .id(maintenanceRequest.getId())
                .title(maintenanceRequest.getTitle())
                .description(maintenanceRequest.getDescription())
                .priority(maintenanceRequest.getPriority())
                .status(maintenanceRequest.getStatus())
                .storeId(maintenanceRequest.getStore() != null ? maintenanceRequest.getStore().getId() : null)
                .storeName(maintenanceRequest.getStore() != null ? maintenanceRequest.getStore().getName() : null)
                .equipmentId(maintenanceRequest.getEquipment() != null ? maintenanceRequest.getEquipment().getId() : null)
                .equipmentName(maintenanceRequest.getEquipment() != null ? maintenanceRequest.getEquipment().getName() : null)
                .createdByUserId(maintenanceRequest.getCreatedBy() != null ? maintenanceRequest.getCreatedBy().getId() : null)
                .createdByUserFullName(maintenanceRequest.getCreatedBy() != null ? maintenanceRequest.getCreatedBy().getFullName() : null)
                .assignedEngineerId(maintenanceRequest.getAssignedEngineer() != null ? maintenanceRequest.getAssignedEngineer().getId() : null)
                .assignedEngineerFullName(maintenanceRequest.getAssignedEngineer() != null ? maintenanceRequest.getAssignedEngineer().getFullName() : null)
                .dueDate(maintenanceRequest.getDueDate())
                .createdAt(maintenanceRequest.getCreatedAt())
                .updatedAt(maintenanceRequest.getUpdatedAt())
                .build();
    }
}
