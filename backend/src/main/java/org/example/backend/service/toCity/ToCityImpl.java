package org.example.backend.service.toCity;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.ToCityDto;
import org.example.backend.entity.ToCity;
import org.example.backend.repository.ToCityRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class ToCityImpl implements ToCityService{
    private final ToCityRepo toCityRepo;

    @Override
    public ResponseEntity<?> getCity() {
        List<ToCity> cities = toCityRepo.findAll();
        return ResponseEntity.ok(cities);
    }

    @Override
    public HttpEntity<?> saveCity(ToCityDto toCityDto) {
        ToCity toCity = new ToCity(toCityDto.getName());
        toCityRepo.save(toCity);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli qo'shildi!");
    }

    @Override
    public HttpEntity<?> EditCity(UUID id, ToCityDto toCityDto) {
        ToCity toCity = toCityRepo.findById(id).orElseThrow();
        toCity.setName(toCityDto.getName());
        toCityRepo.save(toCity);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli tahrirlandi!");

    }

    @Override
    public HttpEntity<?> DeleteRoute(UUID id) {
        toCityRepo.deleteById(id);
        return ResponseEntity.ok("ma'lumot muvaqqiyatli o'chirildi!");

    }
}
