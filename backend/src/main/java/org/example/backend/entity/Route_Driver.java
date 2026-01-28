package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "routes")
public class Route_Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid default gen_random_uuid()")
    private UUID id;
    @Column(name = "from_city")
    private String fromCity;
    @Column(name = "to_city")
    private String toCity;
    private Integer countSide;
    private Integer price;
    private LocalDate day;
    private String hour;
    @ManyToOne
    private User user;
    private List<UUID> passenger;


    public Route_Driver(String fromCity, String toCity, String countSide, String price, String day, String hour, User user) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.countSide = Integer.valueOf(countSide);
        this.price = Integer.valueOf(price);
        this.day = LocalDate.parse(day);
        this.hour = hour;
        this.user = user;
    }

    public Route_Driver(String fromCity, String toCity, Integer countSide, Integer price, LocalDate day, String hour, User user) {
  this.fromCity=fromCity;
  this.toCity=toCity;
  this.countSide=countSide;
  this.price=price;
  this.day=day;
  this.hour=hour;
  this.user=user;
    }

    public Route_Driver(String cityA, String cityB, String number, String number1, LocalDate date, String time, UUID uuid) {
   this.fromCity=cityA;
   this.toCity=cityB;
   this.countSide= Integer.valueOf(number);
   this.price= Integer.valueOf(number1);
   this.day=date;
   this.hour=time;
   this.id=uuid;

    }
}
