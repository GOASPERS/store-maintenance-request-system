package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.AuthResponseDto;
import com.example.maintenancerequests.dto.LoginRequestDto;
import com.example.maintenancerequests.dto.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto dto);

    AuthResponseDto login(LoginRequestDto dto);
}
