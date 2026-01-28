package org.example.backend.repository;

import org.example.backend.entity.Comment;
import org.example.backend.entity.Comment1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CommentRepo1 extends JpaRepository<Comment1, UUID> {
    List<Comment1> findAllByUserId(UUID userId);

}
