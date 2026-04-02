package com.example.maintenancerequests.dto;

import com.example.maintenancerequests.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestStatusDto {

    @NotNull
    private RequestStatus status;
}
