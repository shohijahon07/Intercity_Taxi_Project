package org.example.backend.service;

import org.example.backend.DTO.FromCityDto;
import org.example.backend.entity.FromCity;
import org.example.backend.repository.FromCityRepo;
import org.example.backend.service.fromCity.FromCityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FromCityImplTest {

    @Mock
    private FromCityRepo fromCityRepo;

    @InjectMocks
    private FromCityImpl fromCityImpl;

    private FromCity fromCity;
    private FromCityDto fromCityDto;

    @BeforeEach
    void setUp() {
        fromCity = new FromCity();
        fromCity.setName("Sinov shahri");
        fromCityDto = new FromCityDto();
        fromCityDto.setName("Yangilangan shahar");
    }

    @Test
    void testGetCity() {
        when(fromCityRepo.findAll()).thenReturn(Collections.singletonList(fromCity));
        ResponseEntity<?> response = fromCityImpl.getCity();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(Collections.singletonList(fromCity), response.getBody());
    }

    @Test
    void testSaveCity() {
        when(fromCityRepo.save(any(FromCity.class))).thenReturn(fromCity);
        ResponseEntity<?> response = (ResponseEntity<?>) fromCityImpl.saveCity(fromCityDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("ma'lumot muvaqqiyatli qo'shildi!", response.getBody());
    }

    @Test
    void testEditCity() {
        UUID id = UUID.randomUUID();
        when(fromCityRepo.findById(id)).thenReturn(Optional.of(fromCity));
        when(fromCityRepo.save(any(FromCity.class))).thenReturn(fromCity);
        ResponseEntity<?> response = (ResponseEntity<?>) fromCityImpl.EditCity(id, fromCityDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("ma'lumot muvaqqiyatli tahrirlandi!", response.getBody());
    }

    @Test
    void testDeleteCity() {
        UUID id = UUID.randomUUID();
        doNothing().when(fromCityRepo).deleteById(id);
        ResponseEntity<?> response = (ResponseEntity<?>) fromCityImpl.DeleteRoute(id);
        verify(fromCityRepo).deleteById(id);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("ma'lumot muvaqqiyatli o'chirildi!", response.getBody());
    }
}
