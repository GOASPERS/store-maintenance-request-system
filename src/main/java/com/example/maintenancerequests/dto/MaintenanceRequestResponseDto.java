package com.example.maintenancerequests.dto;

import com.example.maintenancerequests.enums.RequestPriority;
import com.example.maintenancerequests.enums.RequestStatus;
import java.time.LocalDate;
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
public class MaintenanceRequestResponseDto {

    private Long id;
    private String title;
    private String description;
    private RequestPriority priority;
    private RequestStatus status;
    private Long storeId;
    private String storeName;
    private Long equipmentId;
    private String equipmentName;
    private Long createdByUserId;
    private String createdByUserFullName;
    private Long assignedEngineerId;
    private String assignedEngineerFullName;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
