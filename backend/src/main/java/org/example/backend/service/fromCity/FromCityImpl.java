package org.example.backend.service.fromCity;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.FromCityDto;
import org.example.backend.entity.FromCity;
import org.example.backend.entity.ToCity;
import org.example.backend.repository.FromCityRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class FromCityImpl implements FromCityService{
    private final FromCityRepo fromCityRepo;

    @Override
    public ResponseEntity<?> getCity() {
        List<FromCity> cities = fromCityRepo.findAll();
        return ResponseEntity.ok(cities);
    }

    @Override
    public HttpEntity<?> saveCity(FromCityDto fromCityDto) {
        FromCity fromCity = new FromCity(fromCityDto.getName());
        fromCityRepo.save(fromCity);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli qo'shildi!");

    }

    @Override
    public HttpEntity<?> EditCity(UUID id, FromCityDto fromCityDto) {
        FromCity fromCity = fromCityRepo.findById(id).orElseThrow();
        fromCity.setName(fromCityDto.getName());
        fromCityRepo.save(fromCity);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli tahrirlandi!");

    }

    @Override
    public HttpEntity<?> DeleteRoute(UUID id) {
        fromCityRepo.deleteById(id);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli o'chirildi!");

    }
}
