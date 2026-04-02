package com.example.maintenancerequests.dto;

import com.example.maintenancerequests.enums.Role;
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
public class AuthResponseDto {

    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
}
