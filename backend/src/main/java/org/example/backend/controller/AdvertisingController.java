package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.AdvertsiningDTO;
import org.example.backend.DTO.ConnectionDto;
import org.example.backend.service.Advertising.AdvertisingService;
import org.example.backend.service.Advertising.AdvertisingServiceImpl;
import org.example.backend.service.connection.ConnectionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advertising")
@RequiredArgsConstructor
public class AdvertisingController {
    private final AdvertisingService advertisingService;
    @PostMapping
    public HttpEntity<?> saveAdvertising(@RequestBody AdvertsiningDTO advertsiningDTO) {
        advertisingService.saveAdvertising(advertsiningDTO);
        return ResponseEntity.ok("success");
    }

}
