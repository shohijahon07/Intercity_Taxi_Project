package org.example.backend.service.connection;

import org.example.backend.DTO.ConnectionDto;
import org.springframework.http.HttpEntity;

public interface ConnectionService {
    HttpEntity<?> saveConnection(Integer language, ConnectionDto connectionDto);

    HttpEntity<?> getConnection(Integer language);
}
