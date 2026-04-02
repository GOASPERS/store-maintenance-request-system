package com.example.maintenancerequests.mapper;

import com.example.maintenancerequests.dto.CreateStoreRequestDto;
import com.example.maintenancerequests.dto.StoreResponseDto;
import com.example.maintenancerequests.dto.UpdateStoreRequestDto;
import com.example.maintenancerequests.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toEntity(CreateStoreRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return Store.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .contactPerson(dto.getContactPerson())
                .contactPhone(dto.getContactPhone())
                .build();
    }

    public void updateEntity(UpdateStoreRequestDto dto, Store store) {
        if (dto == null || store == null) {
            return;
        }

        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setCity(dto.getCity());
        store.setContactPerson(dto.getContactPerson());
        store.setContactPhone(dto.getContactPhone());
    }

    public StoreResponseDto toResponseDto(Store store) {
        if (store == null) {
            return null;
        }

        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .city(store.getCity())
                .contactPerson(store.getContactPerson())
                .contactPhone(store.getContactPhone())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }
}
