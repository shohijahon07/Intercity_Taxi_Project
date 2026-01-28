package org.example.backend.repository;


import org.example.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
List<Role>findAllByName(String name);

    Role findByName(String roleDriver);
}
