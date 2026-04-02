package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateStoreRequestDto;
import com.example.maintenancerequests.dto.StoreResponseDto;
import com.example.maintenancerequests.dto.UpdateStoreRequestDto;
import java.util.List;

public interface StoreService {

    StoreResponseDto createStore(CreateStoreRequestDto dto);

    List<StoreResponseDto> getAllStores();

    StoreResponseDto getStoreById(Long id);

    StoreResponseDto updateStore(Long id, UpdateStoreRequestDto dto);

    void deleteStore(Long id);
}
