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
@Entity(name = "connection2")
public class Connection2 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;
    private String phoneNumber;
    private  String fullName;
    private String message;

    public Connection2(String phoneNumber,String fullName , String message) {
        this.phoneNumber = phoneNumber;
        this.fullName=fullName;
        this.message = message;
    }
}
