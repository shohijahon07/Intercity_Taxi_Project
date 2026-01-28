package org.example.backend.service.driver;

import org.example.backend.DTO.DriverDto;
import org.example.backend.DTO.IsDriving;
import org.example.backend.DTO.PessengerDto;
import org.example.backend.DTO.UserDto;
import org.example.backend.entity.Role;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    ResponseEntity<?> getDriverOne(UUID id);

    HttpEntity<?> saveAboutDriver(UUID id, String driverAboutDto);

    HttpEntity<?> editDriver(UUID id, DriverDto driverDto);

    ResponseEntity<?> getDriversAll(List<Role> roleDriver, Boolean isDriver);

    HttpEntity<?> editDriverIsDriving(UUID id, IsDriving isDriving) throws IOException;

    ResponseEntity<?> countDriver(List<Role> roleDriver);

    ResponseEntity<?> CountUserAll(List<Role> roleDriver);



    ResponseEntity<?> deleteUser(UUID id);

    HttpEntity<?> savePessenger(PessengerDto pessengerDto) throws IOException;

    ResponseEntity<?> SearchNameDriver(String name);

    ResponseEntity<?> saveUser(String userDtoString, MultipartFile carImg, MultipartFile driverImg, MultipartFile cardDocument);
}
