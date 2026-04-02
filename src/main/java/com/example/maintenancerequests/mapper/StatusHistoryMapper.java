package com.example.maintenancerequests.mapper;

import com.example.maintenancerequests.dto.StatusHistoryResponseDto;
import com.example.maintenancerequests.entity.StatusHistory;
import org.springframework.stereotype.Component;

@Component
public class StatusHistoryMapper {

    public StatusHistoryResponseDto toResponseDto(StatusHistory statusHistory) {
        if (statusHistory == null) {
            return null;
        }

        return StatusHistoryResponseDto.builder()
                .id(statusHistory.getId())
                .maintenanceRequestId(statusHistory.getMaintenanceRequest() != null
                        ? statusHistory.getMaintenanceRequest().getId()
                        : null)
                .oldStatus(statusHistory.getOldStatus())
                .newStatus(statusHistory.getNewStatus())
                .changedByUserId(statusHistory.getChangedBy() != null ? statusHistory.getChangedBy().getId() : null)
                .changedByUserFullName(statusHistory.getChangedBy() != null
                        ? statusHistory.getChangedBy().getFullName()
                        : null)
                .changedAt(statusHistory.getChangedAt())
                .build();
    }
}
