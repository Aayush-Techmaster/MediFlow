package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.AuthResponse;
import com.project.MediFlow.Dtos.LoginRequest;
import com.project.MediFlow.Dtos.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
