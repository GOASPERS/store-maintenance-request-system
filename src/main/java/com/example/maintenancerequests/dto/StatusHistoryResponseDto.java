package com.example.maintenancerequests.dto;

import com.example.maintenancerequests.enums.RequestStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistoryResponseDto {

    private Long id;
    private Long maintenanceRequestId;
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private Long changedByUserId;
    private String changedByUserFullName;
    private LocalDateTime changedAt;
}
