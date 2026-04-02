package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateStoreRequestDto;
import com.example.maintenancerequests.dto.StoreResponseDto;
import com.example.maintenancerequests.dto.UpdateStoreRequestDto;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.StoreMapper;
import com.example.maintenancerequests.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreResponseDto createStore(CreateStoreRequestDto dto) {
        Store store = storeMapper.toEntity(dto);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toResponseDto(savedStore);
    }

    @Override
    public List<StoreResponseDto> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(storeMapper::toResponseDto)
                .toList();
    }

    @Override
    public StoreResponseDto getStoreById(Long id) {
        Store store = findStoreByIdOrThrow(id);
        return storeMapper.toResponseDto(store);
    }

    @Override
    @Transactional
    public StoreResponseDto updateStore(Long id, UpdateStoreRequestDto dto) {
        Store store = findStoreByIdOrThrow(id);
        storeMapper.updateEntity(dto, store);
        Store updatedStore = storeRepository.save(store);
        return storeMapper.toResponseDto(updatedStore);
    }

    @Override
    @Transactional
    public void deleteStore(Long id) {
        Store store = findStoreByIdOrThrow(id);
        storeRepository.delete(store);
    }

    private Store findStoreByIdOrThrow(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
    }
}
