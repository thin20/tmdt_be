package com.example.tmdt_be.service;

public interface EncryptService {
    String encryptPassword(String plainPassword);

    boolean checkPassword(String checkPassword, String encryptedPassword);
}
