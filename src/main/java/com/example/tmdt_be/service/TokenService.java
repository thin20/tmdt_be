package com.example.tmdt_be.service;

import java.util.Optional;

public interface TokenService {
    String generateToken(String subject);

    Optional<String> getSubFromToken(String token);

    boolean isTokenExpired(String token);
}
