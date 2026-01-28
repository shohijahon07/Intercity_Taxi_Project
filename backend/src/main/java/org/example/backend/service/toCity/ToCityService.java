package org.example.backend.service.toCity;

import org.example.backend.DTO.ToCityDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ToCityService {
    ResponseEntity<?> getCity();

    HttpEntity<?> saveCity(ToCityDto toCityDto);

    HttpEntity<?> EditCity(UUID id, ToCityDto toCityDto);

    HttpEntity<?> DeleteRoute(UUID id);
}
