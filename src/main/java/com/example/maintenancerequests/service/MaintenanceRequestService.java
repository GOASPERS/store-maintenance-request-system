package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CreateMaintenanceRequestDto;
import com.example.maintenancerequests.dto.MaintenanceRequestResponseDto;
import com.example.maintenancerequests.dto.StatusHistoryResponseDto;
import com.example.maintenancerequests.dto.UpdateRequestStatusDto;
import com.example.maintenancerequests.dto.UpdateMaintenanceRequestDto;
import com.example.maintenancerequests.enums.RequestStatus;
import java.util.List;

public interface MaintenanceRequestService {

    MaintenanceRequestResponseDto createMaintenanceRequest(CreateMaintenanceRequestDto dto);

    List<MaintenanceRequestResponseDto> getAllMaintenanceRequests(
            RequestStatus status,
            Long storeId,
            Long assignedEngineerId
    );

    MaintenanceRequestResponseDto getMaintenanceRequestById(Long id);

    MaintenanceRequestResponseDto updateMaintenanceRequest(Long id, UpdateMaintenanceRequestDto dto);

    MaintenanceRequestResponseDto updateRequestStatus(Long id, UpdateRequestStatusDto dto);

    List<StatusHistoryResponseDto> getStatusHistoryByRequestId(Long id);

    void deleteMaintenanceRequest(Long id);
}
