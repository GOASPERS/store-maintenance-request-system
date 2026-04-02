package com.example.maintenancerequests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.maintenancerequests.dto.CreateStoreRequestDto;
import com.example.maintenancerequests.dto.StoreResponseDto;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.StoreMapper;
import com.example.maintenancerequests.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void createStore() {
        CreateStoreRequestDto dto = new CreateStoreRequestDto("Main Store", "Address", "City", "John", "+100");
        Store store = Store.builder().name("Main Store").build();
        Store savedStore = Store.builder().id(1L).name("Main Store").build();
        StoreResponseDto responseDto = StoreResponseDto.builder().id(1L).name("Main Store").build();

        when(storeMapper.toEntity(dto)).thenReturn(store);
        when(storeRepository.save(store)).thenReturn(savedStore);
        when(storeMapper.toResponseDto(savedStore)).thenReturn(responseDto);

        StoreResponseDto result = storeService.createStore(dto);

        assertEquals(1L, result.getId());
        verify(storeRepository).save(store);
    }

    @Test
    void getStoreByIdSuccess() {
        Store store = Store.builder().id(1L).name("Main Store").build();
        StoreResponseDto responseDto = StoreResponseDto.builder().id(1L).name("Main Store").build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(storeMapper.toResponseDto(store)).thenReturn(responseDto);

        StoreResponseDto result = storeService.getStoreById(1L);

        assertEquals("Main Store", result.getName());
    }

    @Test
    void getStoreByIdNotFound() {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreById(99L));
    }
}
