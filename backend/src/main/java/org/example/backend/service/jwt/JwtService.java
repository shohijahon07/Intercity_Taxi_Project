package org.example.backend.service.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.example.backend.entity.User;

import javax.crypto.SecretKey;

public interface JwtService {

    String  generateJwtToken(User user);
    String extractSubject(String token);
    String generateJwtRefreshToken(User user);

}
