package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.FromCityDto;
import org.example.backend.DTO.RouteDriverDto;
import org.example.backend.service.fromCity.FromCityService;
import org.example.backend.service.route.DriverRouteService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fromCity")
@RequiredArgsConstructor
public class FromCityController {
    private final FromCityService fromCityService;

    @GetMapping
    public ResponseEntity<?> getRoute(){
        return fromCityService.getCity();
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public HttpEntity<?> saveCity( @RequestBody FromCityDto fromCityDto){
        return fromCityService.saveCity(fromCityDto);
    }
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public HttpEntity<?> EditCity( @RequestParam UUID id, @RequestBody FromCityDto fromCityDto){
        return fromCityService.EditCity(id,fromCityDto);
    }
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public HttpEntity<?> DeleteRoute(@RequestParam UUID id){
        return fromCityService.DeleteRoute( id);
    }
}
