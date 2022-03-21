package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserSdo createUser(CreateUserSdi sdi);

    User findByPhoneNumber(String phoneNumber);

    UserSdo login(LoginByPhoneNumberSdi sdi) throws JsonProcessingException;

    UserSdo loginByToken(String token) throws JsonProcessingException;

    UserSdo findById(Long userId);
}
