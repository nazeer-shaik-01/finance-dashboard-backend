package com.nazeer.finance.service;

import com.nazeer.finance.dto.AuthResponse;
import com.nazeer.finance.dto.LoginRequest;
import com.nazeer.finance.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
