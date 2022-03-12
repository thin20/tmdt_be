package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.service.EncryptService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService implements EncryptService {
    private BCryptPasswordEncoder passwordEncoder;

    public BcryptService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean checkPassword(String checkPassword, String encryptedPassword) {
        return passwordEncoder.matches(checkPassword, encryptedPassword);
    }
}
