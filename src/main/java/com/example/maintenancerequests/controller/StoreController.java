package com.example.maintenancerequests.controller;

import com.example.maintenancerequests.dto.CreateStoreRequestDto;
import com.example.maintenancerequests.dto.StoreResponseDto;
import com.example.maintenancerequests.dto.UpdateStoreRequestDto;
import com.example.maintenancerequests.service.StoreService;
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
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody CreateStoreRequestDto dto) {
        StoreResponseDto createdStore = storeService.createStore(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDto>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStoreRequestDto dto
    ) {
        return ResponseEntity.ok(storeService.updateStore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
