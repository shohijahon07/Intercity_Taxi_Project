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
@Entity(name = "comment1")
public class Comment1 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;
    private String text;
    private String  name;
    private String  phoneNumber;

    @ManyToOne
    private User user;


    public Comment1(String text,String phoneNumber, String name, User user) {
        this.text = text;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.user = user;
    }
}
