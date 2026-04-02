package com.example.maintenancerequests.controller;

import com.example.maintenancerequests.dto.CreateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.MaintenanceRequestResponseDto;
import com.example.maintenancerequests.dto.StatusHistoryResponseDto;
import com.example.maintenancerequests.dto.UpdateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.UpdateRequestStatusDto;
import com.example.maintenancerequests.enums.RequestStatus;
import com.example.maintenancerequests.service.MaintenanceRequestService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class MaintenanceRequestController {

    private final MaintenanceRequestService maintenanceRequestService;

    @PostMapping
    public ResponseEntity<MaintenanceRequestResponseDto> createMaintenanceRequest(
            @Valid @RequestBody CreateMaintenanceRequestDto dto
    ) {
        MaintenanceRequestResponseDto createdRequest = maintenanceRequestService.createMaintenanceRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRequestResponseDto>> getAllMaintenanceRequests(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long assignedEngineerId
    ) {
        return ResponseEntity.ok(
                maintenanceRequestService.getAllMaintenanceRequests(status, storeId, assignedEngineerId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequestResponseDto> getMaintenanceRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceRequestService.getMaintenanceRequestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRequestResponseDto> updateMaintenanceRequest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMaintenanceRequestDto dto
    ) {
        return ResponseEntity.ok(maintenanceRequestService.updateMaintenanceRequest(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MaintenanceRequestResponseDto> updateRequestStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequestStatusDto dto
    ) {
        return ResponseEntity.ok(maintenanceRequestService.updateRequestStatus(id, dto));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StatusHistoryResponseDto>> getRequestStatusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceRequestService.getStatusHistoryByRequestId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRequest(@PathVariable Long id) {
        maintenanceRequestService.deleteMaintenanceRequest(id);
        return ResponseEntity.noContent().build();
    }
}
