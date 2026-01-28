package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "connection1")
public class Connection1 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;
    private String phoneNumber;
    private String fullName;
    private String message;

    public Connection1(String phoneNumber, String fullName , String message) {
        this.phoneNumber = phoneNumber;
        this.fullName=fullName;
        this.message = message;
    }
}
