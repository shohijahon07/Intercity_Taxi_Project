//package org.example.backend.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.backend.DTO.RouteDriverDto;
//import org.example.backend.entity.Route_Driver;
//import org.example.backend.entity.User;
//import org.example.backend.repository.RouteDriverRepo;
//import org.example.backend.repository.UserRepo;
//import org.example.backend.service.route.DriverRouteImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@RequiredArgsConstructor
//public class DriverRouteImplTest {
//
//    @Mock
//    private RouteDriverRepo routeDriverRepo;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @InjectMocks
//    private DriverRouteImpl driverRouteImpl;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetRoute() {
//        Route_Driver routeDriver = new Route_Driver("CityA", "CityB", 4, 100, LocalDate.now(), "10:00", new User(UUID.randomUUID()));
//        when(routeDriverRepo.findAll()).thenReturn(Arrays.asList(routeDriver));
//
//        ResponseEntity<?> response = driverRouteImpl.getRoute();
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody() instanceof List);
//        List<Route_Driver> drivers = (List<Route_Driver>) response.getBody();
//        assertFalse(drivers.isEmpty());
//    }
//
//    @Test
//    public void testSaveRoute() {
//        RouteDriverDto routeDriverDto = new RouteDriverDto("CityA", "CityB", "4", "100", "2024-09-10", "10:00", UUID.randomUUID());
//        Route_Driver routeDriver = new Route_Driver("CityA", "CityB", "4", "100", "2024-09-10", "10:00", UUID.randomUUID());
//
//        // Mock the save method to return the routeDriver object
//        when(routeDriverRepo.save(any(Route_Driver.class))).thenReturn(routeDriver);
//
//        ResponseEntity<?> response = (ResponseEntity<?>) driverRouteImpl.saveRoute(routeDriverDto);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("save succesfull", response.getBody());
//    }
//
//    @Test
//    public void testEditRoute() {
//        UUID id = UUID.randomUUID();
//        RouteDriverDto routeDriverDto = new RouteDriverDto("CityA", "CityB", "4", "100", "2024-09-10", "10:00", UUID.randomUUID());
//        Route_Driver routeDriver = new Route_Driver("CityA", "CityB", 4, 100, LocalDate.now(), "10:00", new User(UUID.randomUUID()));
//
//        when(routeDriverRepo.findById(id)).thenReturn(Optional.of(routeDriver));
//        doNothing().when(routeDriverRepo).save(any(Route_Driver.class));
//
//        ResponseEntity<?> response = (ResponseEntity<?>) driverRouteImpl.EditRoute(id, routeDriverDto);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("edit Successfull", response.getBody());
//    }
//
//    @Test
//    public void testDeleteRoute() {
//        UUID id = UUID.randomUUID();
//        doNothing().when(routeDriverRepo).deleteById(id);
//
//        ResponseEntity<?> response = (ResponseEntity<?>) driverRouteImpl.DeleteRoute(id);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("edit succesful", response.getBody());
//    }
//
//    @Test
//    public void testGetRouteByDriver() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id);
//        Route_Driver routeDriver = new Route_Driver("CityA", "CityB", 4, 100, LocalDate.now(), "10:00", user);
//
//        when(userRepo.findById(id)).thenReturn(Optional.of(user));
//        when(routeDriverRepo.findAllByUser(user)).thenReturn(Arrays.asList(routeDriver));
//
//        ResponseEntity<?> response = driverRouteImpl.getRouteByDriver(id);
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody() instanceof List);
//        List<Route_Driver> drivers = (List<Route_Driver>) response.getBody();
//        assertFalse(drivers.isEmpty());
//    }
//
//    @Test
//    public void testGetRouteByDate() {
//        RouteDriverDto routeDriverDto = new RouteDriverDto("CityA", "CityB", "4", "100", "2024-09-10", "10:00", UUID.randomUUID());
//        when(routeDriverRepo.findAllByDayAndFromCityAndToCity(LocalDate.parse("2024-09-10"), "CityA", "CityB"))
//                .thenReturn(Arrays.asList(new Route_Driver("CityA", "CityB", 4, 100, LocalDate.now(), "10:00", new User(UUID.randomUUID()))));
//
//        ResponseEntity<?> response = driverRouteImpl.getRouteByDate(routeDriverDto);
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody() instanceof List);
//    }
//
//    @Test
//    public void testGetRouteByDay() {
//        LocalDate today = LocalDate.now();
//        LocalDate needDay = today;
//        Integer day = 2;
//
//        Route_Driver routeDriver = new Route_Driver("CityA", "CityB", 4, 100, needDay.plusDays(1), "10:00", new User(UUID.randomUUID()));
//        when(routeDriverRepo.findAllByDayAndFromCityAndToCity(needDay.plusDays(1), "CityA", "CityB"))
//                .thenReturn(Arrays.asList(routeDriver));
//
//
//        ResponseEntity<?> response = driverRouteImpl.getRouteByDay(day);
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody() instanceof List);
//        List<Route_Driver> drivers = (List<Route_Driver>) response.getBody();
//        assertFalse(drivers.isEmpty());
//    }
//}
