package org.example.backend.DTO;

import lombok.Data;

@Data
public class UserDto {
    private String fullName;
    private boolean count;
    private String phoneNumber;
    private String carType;
    private String carImg;
    private String driverImg;
    private String cardDocument;
    private String about;
    private Long chatId;
}
