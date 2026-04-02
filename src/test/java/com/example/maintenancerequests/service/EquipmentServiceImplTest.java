package com.example.maintenancerequests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.maintenancerequests.dto.CreateEquipmentRequestDto;
import com.example.maintenancerequests.dto.EquipmentResponseDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.EquipmentMapper;
import com.example.maintenancerequests.repository.EquipmentRepository;
import com.example.maintenancerequests.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Test
    void createEquipmentWithValidStore() {
        CreateEquipmentRequestDto dto = new CreateEquipmentRequestDto("Printer", "Office", "SN-1", 1L);
        Store store = Store.builder().id(1L).name("Main Store").build();
        Equipment equipment = Equipment.builder().name("Printer").store(store).build();
        Equipment savedEquipment = Equipment.builder().id(10L).name("Printer").store(store).build();
        EquipmentResponseDto responseDto = EquipmentResponseDto.builder().id(10L).name("Printer").storeId(1L).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(equipmentMapper.toEntity(dto, store)).thenReturn(equipment);
        when(equipmentRepository.save(equipment)).thenReturn(savedEquipment);
        when(equipmentMapper.toResponseDto(savedEquipment)).thenReturn(responseDto);

        EquipmentResponseDto result = equipmentService.createEquipment(dto);

        assertEquals(10L, result.getId());
        verify(equipmentRepository).save(equipment);
    }

    @Test
    void createEquipmentWhenStoreNotFound() {
        CreateEquipmentRequestDto dto = new CreateEquipmentRequestDto("Printer", "Office", "SN-1", 1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.createEquipment(dto));
    }
}
