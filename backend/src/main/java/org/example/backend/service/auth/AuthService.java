package org.example.backend.service.auth;

import org.example.backend.DTO.LoginDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface AuthService {


    HttpEntity<?> loginUser(LoginDto loginDto);

    HttpEntity<?> refreshTokenFunc(String refreshToken);

    ResponseEntity<?> checkUserRole(String authorization);

    ResponseEntity<?> checkUserName(String authorization);
}
