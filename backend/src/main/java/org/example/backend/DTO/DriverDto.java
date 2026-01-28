package org.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private String fullName;
    private String phoneNumber;
    private String carType;
    private String carImg;
    private String driverImg;
    private String cardDocument;
    private String about;
}
