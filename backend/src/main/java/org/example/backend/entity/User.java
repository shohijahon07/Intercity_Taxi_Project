package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private Long chatId;
    private String carType;
    private String carImg;
    private String driverImg;
    private String cardDocument;
    private String about;
    private boolean count;
    private Boolean isDriver=false;
    private String language;
    private String commen_id;
    @Enumerated(EnumType.STRING)
    private Status status=Status.START;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    public User(String fullName, String phoneNumber, String password, Long chatId, String carType, String carImg, String driverImg, String cardDocument, Boolean isDriver, Status status, List<Role> roles) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.chatId = chatId;
        this.carType = carType;
        this.carImg = carImg;
        this.driverImg = driverImg;
        this.cardDocument = cardDocument;
        this.isDriver = isDriver;
        this.status = status;
        this.roles = roles;
    }


    public User(UUID id) {
        this.id = id;
    }



    public User( Long chatID, Status status , String phoneNumber) {
        this.chatId = chatID;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    public User( Long chatId,String password) {
        this.chatId = chatId;
        this.password = password;
    }

    public User(String fullName, String carType, String carImg, String driverImg, String cardDocument,Long chatId, List<Role> roles) {
        this.fullName = fullName;
        this.carType = carType;
        this.carImg = carImg;
        this.driverImg = driverImg;
        this.cardDocument = cardDocument;
        this.chatId=chatId;
        this.roles = roles;
    }

    public User(Long chatId) {
        this.chatId = chatId;
    }

    public User(String fullName, String phoneNumber,List<Role> roles) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return fullName;
    }

    public void setStatus2(Status status) {
        this.status = status;
    }

    public Status getStatus2() {
        return status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
