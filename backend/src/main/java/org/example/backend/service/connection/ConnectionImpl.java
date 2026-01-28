package org.example.backend.service.connection;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.ConnectionDto;
import org.example.backend.entity.Connection1;
import org.example.backend.entity.Connection2;
import org.example.backend.repository.ConnectionRepo1;
import org.example.backend.repository.ConnectionRepo2;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionImpl implements ConnectionService{
    private final ConnectionRepo1 connectionRepo1;
    private final ConnectionRepo2 connectionRepo2;



    @Override
    public HttpEntity<?> saveConnection(Integer language, ConnectionDto connectionDto) {
        if (language == 1) {
            Connection1 connection1 = new Connection1(connectionDto.getPhoneNumber(),connectionDto.getFullName() , connectionDto.getMessage());
            connectionRepo1.save(connection1);
            return ResponseEntity.ok("muvaffaqiyatli qo'shildi");

        } else if (language == 2) {
            Connection2 connection2 = new Connection2(connectionDto.getPhoneNumber(),connectionDto.getFullName() , connectionDto.getMessage());
            connectionRepo2.save(connection2);
            return ResponseEntity.ok("успешно добавлено");

        }
        return null;
    }

    @Override
    public HttpEntity<?> getConnection(Integer language) {
        if(language==1){
            List<Connection1> all = connectionRepo1.findAll();
            return ResponseEntity.ok(all);
        }else if(language==2){
            List<Connection2> all = connectionRepo2.findAll();
            return ResponseEntity.ok(all);
        }
        return null;
    }
}
