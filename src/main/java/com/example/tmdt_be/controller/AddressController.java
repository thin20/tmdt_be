package com.example.tmdt_be.controller;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping(value="getAddressDefault")
    public ResponseEntity<Address> getAddressDefault(@RequestParam (value = "userId", required = true) Long userId) {
        return ResponseEntity.ok(addressService.getAddressDefault(userId));
    }
}
