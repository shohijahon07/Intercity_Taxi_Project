package org.example.backend.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.entity.Comment;
import org.example.backend.entity.Comment1;
import org.example.backend.repository.CommentRepo;
import org.example.backend.repository.CommentRepo1;
import org.example.backend.repository.RoleRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ComentController {
    final CommentRepo commentRepo;
    final CommentRepo1 commentRepo1;

    @GetMapping
    public ResponseEntity<?> checkUserRoles(@RequestParam Integer language,@RequestParam UUID id) {
        if (language==1){
            List<Comment> comments = commentRepo.findAllByUserId(id);
            return ResponseEntity.ok(comments);
        }else if (language==2){
            List<Comment1> comments = commentRepo1.findAllByUserId(id);
            return ResponseEntity.ok(comments);
        }
       return null;
    }

    @DeleteMapping
    public HttpEntity<?> DeleteComment(@RequestParam Integer language,@RequestParam UUID id){
        if (language==1){
            commentRepo.deleteById(id);
            return ResponseEntity.ok("Comment deleted");
        }else if (language==2){
            commentRepo1.deleteById(id);
            return ResponseEntity.ok("Comment deleted");
        }
        return null;
    }
}
