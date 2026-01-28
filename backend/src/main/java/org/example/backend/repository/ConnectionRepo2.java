package org.example.backend.repository;

import org.example.backend.entity.Connection1;
import org.example.backend.entity.Connection2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConnectionRepo2 extends JpaRepository<Connection2, UUID> {
}
