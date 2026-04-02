package com.example.maintenancerequests.mapper;

import com.example.maintenancerequests.dto.CreateEquipmentRequestDto;
import com.example.maintenancerequests.dto.EquipmentResponseDto;
import com.example.maintenancerequests.dto.UpdateEquipmentRequestDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public Equipment toEntity(CreateEquipmentRequestDto dto, Store store) {
        if (dto == null) {
            return null;
        }

        return Equipment.builder()
                .name(dto.getName())
                .type(dto.getType())
                .serialNumber(dto.getSerialNumber())
                .store(store)
                .build();
    }

    public void updateEntity(UpdateEquipmentRequestDto dto, Equipment equipment, Store store) {
        if (dto == null || equipment == null) {
            return;
        }

        equipment.setName(dto.getName());
        equipment.setType(dto.getType());
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setStore(store);
    }

    public EquipmentResponseDto toResponseDto(Equipment equipment) {
        if (equipment == null) {
            return null;
        }

        return EquipmentResponseDto.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .type(equipment.getType())
                .serialNumber(equipment.getSerialNumber())
                .storeId(equipment.getStore() != null ? equipment.getStore().getId() : null)
                .storeName(equipment.getStore() != null ? equipment.getStore().getName() : null)
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}
