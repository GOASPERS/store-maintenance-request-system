package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateEquipmentRequestDto;
import com.example.maintenancerequests.dto.EquipmentResponseDto;
import com.example.maintenancerequests.dto.UpdateEquipmentRequestDto;
import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.EquipmentMapper;
import com.example.maintenancerequests.repository.EquipmentRepository;
import com.example.maintenancerequests.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final StoreRepository storeRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    @Transactional
    public EquipmentResponseDto createEquipment(CreateEquipmentRequestDto dto) {
        Store store = findStoreByIdOrThrow(dto.getStoreId());
        Equipment equipment = equipmentMapper.toEntity(dto, store);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponseDto(savedEquipment);
    }

    @Override
    public List<EquipmentResponseDto> getAllEquipment() {
        return equipmentRepository.findAll()
                .stream()
                .map(equipmentMapper::toResponseDto)
                .toList();
    }

    @Override
    public EquipmentResponseDto getEquipmentById(Long id) {
        Equipment equipment = findEquipmentByIdOrThrow(id);
        return equipmentMapper.toResponseDto(equipment);
    }

    @Override
    @Transactional
    public EquipmentResponseDto updateEquipment(Long id, UpdateEquipmentRequestDto dto) {
        Equipment equipment = findEquipmentByIdOrThrow(id);
        Store store = findStoreByIdOrThrow(dto.getStoreId());
        equipmentMapper.updateEntity(dto, equipment, store);
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponseDto(updatedEquipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        Equipment equipment = findEquipmentByIdOrThrow(id);
        equipmentRepository.delete(equipment);
    }

    @Override
    public List<EquipmentResponseDto> getEquipmentByStoreId(Long storeId) {
        findStoreByIdOrThrow(storeId);

        return equipmentRepository.findByStoreId(storeId)
                .stream()
                .map(equipmentMapper::toResponseDto)
                .toList();
    }

    private Equipment findEquipmentByIdOrThrow(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
    }

    private Store findStoreByIdOrThrow(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
    }
}
