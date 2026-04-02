package com.example.maintenancerequests.dto;

import com.example.maintenancerequests.enums.RequestPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequestDto {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private RequestPriority priority;

    @NotNull
    private Long storeId;

    private Long equipmentId;
    private Long assignedEngineerId;
    private LocalDate dueDate;
}
