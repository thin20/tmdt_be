package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.sdi.ChangePasswordSdi;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdi.UpdateUserInfoSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(value="register")
    public ResponseEntity<UserSdo> createUser(@Valid @RequestBody CreateUserSdi sdi) {
        return ResponseEntity.ok(userService.createUser(sdi));
    }

    @PostMapping(value="login")
    public ResponseEntity<UserSdo> login(@Valid @RequestBody LoginByPhoneNumberSdi sdi) throws JsonProcessingException {
        return ResponseEntity.ok(userService.login(sdi));
    }

    @PostMapping(value="loginByToken")
    public ResponseEntity<UserSdo> loginByToken(@Valid @RequestBody String token) throws JsonProcessingException {
        return ResponseEntity.ok(userService.loginByToken(token));
    }

    @PutMapping(value="changePassword")
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody ChangePasswordSdi sdi,
                                                  @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(userService.changePassword(token, sdi));
    }

    @PutMapping(value="updateUserInfo")
    public ResponseEntity<Boolean> updateUserInfo(@Valid @RequestBody UpdateUserInfoSdi sdi,
                                                  @RequestPart ("files") List<MultipartFile> files,
                                                  @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(userService.updateUserInfo(token, sdi, files));
    }

}
