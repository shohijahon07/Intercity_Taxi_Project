package org.example.backend.service.route;

import org.example.backend.DTO.RouteDriverDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DriverRouteService {
    ResponseEntity<?> getRoute();

    HttpEntity<?> saveRoute(RouteDriverDto routeDriverDto);

    HttpEntity<?> EditRoute(UUID id, RouteDriverDto routeDriverDto);

    HttpEntity<?> DeleteRoute(UUID id);

    ResponseEntity<?> getRouteByDriver(UUID id);

    ResponseEntity<?> getRouteByDate(RouteDriverDto day);

    ResponseEntity<?> getRouteByDay(Integer day);

    HttpEntity<?> DeleteByDay(String day, String hour);

    HttpEntity<?> DeleteBytTime();
}
