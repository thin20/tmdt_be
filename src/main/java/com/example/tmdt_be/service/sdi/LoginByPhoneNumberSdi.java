package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginByPhoneNumberSdi {
    private String phoneNumber;
    private String password;
}