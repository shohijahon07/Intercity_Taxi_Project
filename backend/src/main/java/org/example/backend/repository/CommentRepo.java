package org.example.backend.repository;

import org.example.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepo extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByUserId(UUID userId);
}
