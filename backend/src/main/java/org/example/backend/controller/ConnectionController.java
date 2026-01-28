package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.ConnectionDto;
import org.example.backend.service.connection.ConnectionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class ConnectionController {

    private final  ConnectionService connectionService;

    @PostMapping()
    public HttpEntity<?> saveUser(@RequestParam Integer language,@RequestBody ConnectionDto connectionDto) {
        System.out.println(connectionDto);
        connectionService.saveConnection(language,connectionDto);
        return ResponseEntity.ok("success");
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public  HttpEntity<?> getConnection(@RequestParam Integer language){
    HttpEntity<?> connection = connectionService.getConnection(language);
    return ResponseEntity.ok(connection);
}
}
