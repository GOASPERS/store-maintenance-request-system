package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateEquipmentRequestDto;
import com.example.maintenancerequests.dto.EquipmentResponseDto;
import com.example.maintenancerequests.dto.UpdateEquipmentRequestDto;
import java.util.List;

public interface EquipmentService {

    EquipmentResponseDto createEquipment(CreateEquipmentRequestDto dto);

    List<EquipmentResponseDto> getAllEquipment();

    EquipmentResponseDto getEquipmentById(Long id);

    EquipmentResponseDto updateEquipment(Long id, UpdateEquipmentRequestDto dto);

    void deleteEquipment(Long id);

    List<EquipmentResponseDto> getEquipmentByStoreId(Long storeId);
}
