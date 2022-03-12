package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.service.TokenService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService implements TokenService {
    private String secret;
    private int sessionTime;

    @Autowired
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.sessionTime}") int sessionTime) {
        this.secret = secret;
        this.sessionTime = sessionTime;
    }

    @Override
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public Optional<String> getSubFromToken(String token) throws JwtException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return Optional.ofNullable(claimsJws.getBody().getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token , Function<Claims, T> claimResolver) {
        final Claims claim= extractAllClaims(token);
        return claimResolver.apply(claim);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime * 1000);
    }

}
