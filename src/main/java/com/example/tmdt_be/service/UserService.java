package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.service.sdi.ChangePasswordSdi;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdi.UpdateUserInfoSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    UserSdo createUser(CreateUserSdi sdi);

    User findByPhoneNumber(String phoneNumber);

    UserSdo login(LoginByPhoneNumberSdi sdi) throws JsonProcessingException;

    UserSdo loginByToken(String token) throws JsonProcessingException;

    UserSdo findById(Long userId);

    Long getUserIdByBearerToken(String token) throws JsonProcessingException;

    UserSdo getUserByBearerToken(String token) throws JsonProcessingException;

    Boolean changePassword(String token, ChangePasswordSdi sdi) throws JsonProcessingException;

    Boolean updateUserInfo(String token, UpdateUserInfoSdi sdi, List<MultipartFile> files) throws JsonProcessingException;

    Boolean changeAvatar(String token, MultipartFile avatar) throws JsonProcessingException;
}
