package com.example.tmdt_be.controller;

import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value="createUser")
    public ResponseEntity<UserSdo> createUser(@Valid @RequestBody CreateUserSdi sdi) {
        return ResponseEntity.ok(userService.createUser(sdi));
    }

    @PostMapping(value="login")
    public ResponseEntity<UserSdo> login(@Valid @RequestBody LoginByPhoneNumberSdi sdi) throws JsonProcessingException {
        return ResponseEntity.ok(userService.login(sdi));
    }
}
