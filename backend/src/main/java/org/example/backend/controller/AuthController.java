package org.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.LoginDto;
import org.example.backend.DTO.PessengerDto;
import org.example.backend.repository.RoleRepo;
import org.example.backend.repository.UserRepo;
import org.example.backend.service.auth.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserRepo userRepo;
    final PasswordEncoder passwordEncoder;
    final RoleRepo roleRepo;
    final AuthService authService;


    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        System.out.println(loginDto);
        return authService.loginUser(loginDto);
    }


    @PostMapping("/refresh")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public HttpEntity<?> refresh(@RequestHeader String refreshToken){
        return authService.refreshTokenFunc(refreshToken);
    }

    @GetMapping("/check")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<?> checkUserRoles(@RequestHeader(defaultValue = "null") String Authorization){
        return authService.checkUserRole(Authorization);
    }

    @GetMapping("/name")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<?> checkUserName(@RequestHeader(defaultValue = "null") String Authorization){
        return authService.checkUserName(Authorization);
    }
}
