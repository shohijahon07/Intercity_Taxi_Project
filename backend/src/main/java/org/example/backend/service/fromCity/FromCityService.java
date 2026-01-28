package org.example.backend.service.fromCity;

import org.example.backend.DTO.FromCityDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FromCityService {
    ResponseEntity<?> getCity();

    HttpEntity<?> saveCity(FromCityDto fromCityDto);

    HttpEntity<?> EditCity(UUID id, FromCityDto fromCityDto);

    HttpEntity<?> DeleteRoute(UUID id);
}
