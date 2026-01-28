package org.example.backend.repository;


import org.example.backend.entity.FromCity;
import org.example.backend.entity.ToCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToCityRepo extends JpaRepository<ToCity, UUID> {

}
