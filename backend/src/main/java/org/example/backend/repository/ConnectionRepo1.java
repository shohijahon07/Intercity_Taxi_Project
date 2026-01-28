package org.example.backend.repository;

import org.example.backend.entity.Connection1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConnectionRepo1 extends JpaRepository<Connection1, UUID> {
}
