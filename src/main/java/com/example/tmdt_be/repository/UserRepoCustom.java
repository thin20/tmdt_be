package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.service.sdi.CreateUserSdi;

import java.util.Optional;

public interface UserRepoCustom {
    Optional<User> findById(String id);

    User findByPhoneNumber(String phoneNumber);

    Boolean isExistsByPhoneNumber(String phoneNumber);

    void createUser(CreateUserSdi sdi);
}
