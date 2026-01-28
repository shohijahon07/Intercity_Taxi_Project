package org.example.backend.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.backend.entity.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateJwtToken(User user) {
        Map<String, String> map = Map.of("username", user.getUsername());
        String compact = Jwts.builder()
                .claims(map)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .subject(user.getId().toString())
                .signWith(signWithKey())
                .compact();
        return compact;
    }
    @Override
    public String generateJwtRefreshToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60*24))
                .subject(user.getId().toString())
                .signWith(signWithKey())
                .compact();

    }

    @Override
    public String extractSubject(String token) {

        return   Jwts.parser()
                .verifyWith(signWithKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public SecretKey signWithKey(){
        String secretKey="/SUB5NZMAqWyOqhsWNTtCUNEtmRlstJKt4F/iG27ZnY=";
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }

//    @SneakyThrows
//    private String generateBase64SecretKey(){
//        String secretKey="khamroyev1999";
//        String message="your_message";
//
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(secretKeySpec);
//        byte[] hashBytes = mac.doFinal(message.getBytes());
//        return Base64.getEncoder().encodeToString(hashBytes);
//    }
}
