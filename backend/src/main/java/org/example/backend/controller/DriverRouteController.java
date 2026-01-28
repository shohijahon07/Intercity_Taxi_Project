package org.example.backend.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.RouteDriverDto;
import org.example.backend.service.route.DriverRouteService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverRouteController {
    private final DriverRouteService driverRouteService;

    @GetMapping
    public ResponseEntity<?> getRoute(){
        return driverRouteService.getRoute();
    }

    @GetMapping("/byDay")
    public ResponseEntity<?> getRouteByDay(@RequestParam Integer day){
        return driverRouteService.getRouteByDay(day);
    }
    @PutMapping("/byDate")
    public ResponseEntity<?> getRouteByDate(@RequestBody RouteDriverDto routeDriverDto){
        System.out.println(routeDriverDto);
        return driverRouteService.getRouteByDate(routeDriverDto);
    }


     @GetMapping("/bydriver")
    public ResponseEntity<?> getRouteByDriver(@RequestParam UUID id){
         System.out.println(id);
        return driverRouteService.getRouteByDriver(id);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public HttpEntity<?> saveRoute( @RequestBody RouteDriverDto routeDriverDto){
        System.out.println(routeDriverDto);
        return driverRouteService.saveRoute(routeDriverDto);
    }
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public HttpEntity<?> EditRoute( @RequestParam UUID id, @RequestBody RouteDriverDto routeDriverDto){
        return driverRouteService.EditRoute( id,routeDriverDto);
    }
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public HttpEntity<?> DeleteRoute(@RequestParam UUID id){
        return driverRouteService.DeleteRoute( id);
    }
    @DeleteMapping("/bydel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public HttpEntity<?> DeleteByDay(@RequestParam String day,@RequestParam String hour){
        return driverRouteService.DeleteByDay(day,hour);
    }
    @DeleteMapping("/byTime")
    @Transactional
    public HttpEntity<?> DeleteByTime(){
        return driverRouteService.DeleteBytTime();
    }
}
