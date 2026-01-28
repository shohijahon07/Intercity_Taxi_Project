package org.example.backend.service;

import org.example.backend.DTO.ToCityDto;
import org.example.backend.entity.ToCity;
import org.example.backend.repository.ToCityRepo;
import org.example.backend.service.toCity.ToCityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ToCityImplTest {

    @Mock
    private ToCityRepo toCityRepo;

    @InjectMocks
    private ToCityImpl toCityImpl;

    private ToCity toCity;
    private ToCityDto toCityDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        toCity = new ToCity("CityA");
        toCityDto = new ToCityDto("CityA");
    }

    @Test
    public void testGetCity() {
        when(toCityRepo.findAll()).thenReturn(Arrays.asList(toCity));

        ResponseEntity<?> response = toCityImpl.getCity();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        List<ToCity> cities = (List<ToCity>) response.getBody();
        assertFalse(cities.isEmpty());
        assertEquals("CityA", cities.get(0).getName());
    }

    @Test
    public void testSaveCity() {
        ToCityDto newToCityDto = new ToCityDto("CityB");
        ToCity newToCity = new ToCity("CityB");

        // Mock the save method to return the newToCity object
        when(toCityRepo.save(any(ToCity.class))).thenReturn(newToCity);

        ResponseEntity<?> response = (ResponseEntity<?>) toCityImpl.saveCity(newToCityDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ma'lumot muvaqqiyatli qo'shildi!", response.getBody());
    }

    @Test
    public void testEditCity() {
        UUID id = UUID.randomUUID();
        ToCity updatedToCity = new ToCity("CityB");
        updatedToCity.setId(id);

        when(toCityRepo.findById(id)).thenReturn(Optional.of(toCity));
        when(toCityRepo.save(any(ToCity.class))).thenReturn(updatedToCity);

        ResponseEntity<?> response = (ResponseEntity<?>) toCityImpl.EditCity(id, toCityDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ma'lumot muvaqqiyatli tahrirlandi!", response.getBody());
    }

    @Test
    public void testDeleteCity() {
        UUID id = UUID.randomUUID();
        doNothing().when(toCityRepo).deleteById(id);

        ResponseEntity<?> response = (ResponseEntity<?>) toCityImpl.DeleteRoute(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ma'lumot muvaqqiyatli o'chirildi!", response.getBody());
    }
}
