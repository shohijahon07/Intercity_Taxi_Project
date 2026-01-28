package org.example.backend.service.route;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.RouteDriverDto;
import org.example.backend.entity.Route_Driver;
import org.example.backend.entity.User;
import org.example.backend.repository.RouteDriverRepo;
import org.example.backend.repository.UserRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class DriverRouteImpl implements DriverRouteService {
    private final RouteDriverRepo routeDriverRepo;
    private final UserRepo userRepo;
    private String from;
    private String to;
    private LocalDate needDay;

    @Override
    public ResponseEntity<?> getRoute() {
        List<Route_Driver> drivers = routeDriverRepo.findAll();
        return ResponseEntity.ok(drivers);
    }

    @Override
    public HttpEntity<?> saveRoute(RouteDriverDto routeDriverDto) {
        System.out.println(routeDriverDto);
        Route_Driver routeDriver = new Route_Driver(routeDriverDto.getFromCity(), routeDriverDto.getToCity(), routeDriverDto.getCountSide(), routeDriverDto.getPrice(), routeDriverDto.getDay(), routeDriverDto.getHour(), new User(routeDriverDto.getUserId()));
        routeDriverRepo.save(routeDriver);

        return ResponseEntity.ok("save succesfull");
    }

    @Override
    public HttpEntity<?> EditRoute(UUID id, RouteDriverDto routeDriverDto) {
        Route_Driver routeDriver = routeDriverRepo.findById(id).orElseThrow();

        routeDriver.setCountSide(Integer.valueOf(routeDriverDto.getCountSide()));
        routeDriver.setDay(LocalDate.parse(routeDriverDto.getDay()));
        routeDriver.setHour(String.valueOf(routeDriverDto.getHour()));
        routeDriver.setPrice(Integer.valueOf(routeDriverDto.getPrice()));
        routeDriver.setFromCity(routeDriverDto.getFromCity());
        routeDriver.setToCity(routeDriverDto.getToCity());

        routeDriverRepo.save(routeDriver);
        return ResponseEntity.ok("edit Successfull");
    }

    @Override
    public HttpEntity<?> DeleteRoute(UUID id) {
        routeDriverRepo.deleteById(id);
        return ResponseEntity.ok("edit succesful");
    }

    @Override
    public ResponseEntity<?> getRouteByDriver(UUID id) {
        User user = userRepo.findById(id).orElseThrow();
        List<Route_Driver> drivers = routeDriverRepo.findAllByUser(user);

        return ResponseEntity.ok(drivers);
    }

    @Override
    public ResponseEntity<?> getRouteByDate(RouteDriverDto day) {
        from = day.getFromCity();
        to = day.getToCity();
        needDay = LocalDate.parse(day.getDay());
        List<Route_Driver> city = routeDriverRepo.findAllByDayAndFromCityAndToCity(LocalDate.parse(day.getDay()), day.getFromCity(), day.getToCity());
        return ResponseEntity.ok(city);
    }

    @Override
    public ResponseEntity<?> getRouteByDay(Integer day) {
        List<Route_Driver> city;
        LocalDate today = LocalDate.now();
        LocalDate targetDate;


        try {
            if (needDay.equals(today)) {
                targetDate = switch (day) {
                    case 1 -> needDay;
                    case 2 -> needDay.plusDays(1);
                    case 3 -> needDay.plusDays(2);
                    default -> throw new IllegalArgumentException("Invalid day parameter");
                };
            } else if (needDay.equals(today.plusDays(1))) {
                targetDate = switch (day) {
                    case 1 -> needDay.minusDays(1);
                    case 2 -> needDay;
                    case 3 -> needDay.plusDays(1);
                    default -> throw new IllegalArgumentException("Invalid day parameter");
                };
            } else if (needDay.equals(today.plusDays(2))) {
                targetDate = switch (day) {
                    case 1 -> needDay.minusDays(2);
                    case 2 -> needDay.minusDays(1);
                    case 3 -> needDay;
                    default -> throw new IllegalArgumentException("Invalid day parameter");
                };
            } else {
                return ResponseEntity.badRequest().body("Invalid needDay parameter");
            }

            city = routeDriverRepo.findAllByDayAndFromCityAndToCity(targetDate, from, to);


            return ResponseEntity.ok(city);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Logging unexpected exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @Transactional
    @Override
    public HttpEntity<?> DeleteByDay(String day, String hour) {
       LocalDate today = LocalDate.parse(day);
        LocalTime time = LocalTime.parse(hour);
        int dayOfYear1 = today.getDayOfYear();
        LocalDate now=LocalDate.now();
        int dayOfYear = now.getDayOfYear();
        int minute = time.getMinute();
        LocalTime now1 = LocalTime.now();
        int minute1 = now1.getMinute();

        if (dayOfYear1<dayOfYear) {
            routeDriverRepo.deleteByDay(LocalDate.parse(day));
        } else if (dayOfYear1==dayOfYear && minute1>=minute) {
            routeDriverRepo.deleteByDay(LocalDate.parse(day));
        }

        return ResponseEntity.ok("Muvaffaqiyatli o'chirildi!");
    }

    @Transactional
    @Override
    public HttpEntity<?> DeleteBytTime() {
        try {
            LocalDate today = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            int currentMinutes = currentTime.getHour() * 60 + currentTime.getMinute();

            List<Route_Driver> allRoutes = routeDriverRepo.findAll();
            boolean anyDeleted = false;

            for (Route_Driver routeDriver : allRoutes) {
                try {
                    LocalDate routeDay = routeDriver.getDay();
                    LocalTime routeTime = LocalTime.parse(routeDriver.getHour()); // Bu yerda xato bo'lishi mumkin
                    int routeMinutes = routeTime.getHour() * 60 + routeTime.getMinute();

                    if (today.equals(routeDay) && currentMinutes > routeMinutes) {
                        routeDriverRepo.deleteByDayAndHourContainingIgnoreCase(routeDay, routeDriver.getHour());
                        anyDeleted = true;
                    }
                } catch (Exception e) {
                    // Har bir marshrutni o'chirishda yuzaga keladigan xatolarni ushlaymiz
                    System.out.println("Marshrutni o'chirishda xatolik: " + e.getMessage());
                }
            }

            if (anyDeleted) {
                return ResponseEntity.ok("Marshrutlar o'chirildi.");
            } else {
                return ResponseEntity.ok("O'chirish uchun marshrutlar topilmadi.");
            }
        } catch (Exception e) {
            // Umumiy xatolarni ushlash
            System.out.println("Xatolik yuz berdi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xatolik yuz berdi, keyinroq urinib ko'ring.");
        }
    }
}
