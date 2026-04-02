package com.example.maintenancerequests.controller;

import com.example.maintenancerequests.dto.CreateEquipmentRequestDto;
import com.example.maintenancerequests.dto.EquipmentResponseDto;
import com.example.maintenancerequests.dto.UpdateEquipmentRequestDto;
import com.example.maintenancerequests.service.EquipmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentResponseDto> createEquipment(@Valid @RequestBody CreateEquipmentRequestDto dto) {
        EquipmentResponseDto createdEquipment = equipmentService.createEquipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipment);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDto>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEquipmentRequestDto dto
    ) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<EquipmentResponseDto>> getEquipmentByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(equipmentService.getEquipmentByStoreId(storeId));
    }
}
