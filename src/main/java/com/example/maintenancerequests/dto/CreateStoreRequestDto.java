package com.example.maintenancerequests.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequestDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String address;

    @NotBlank
    @Size(max = 150)
    private String city;

    @NotBlank
    @Size(max = 255)
    private String contactPerson;

    @NotBlank
    @Size(max = 50)
    private String contactPhone;
}
