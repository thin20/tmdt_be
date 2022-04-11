package com.example.tmdt_be.controller;

import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.service.AddressService;
import com.example.tmdt_be.service.sdi.CreateUserAddressSdi;
import com.example.tmdt_be.service.sdi.SetAddressDefaultSdi;
import com.example.tmdt_be.service.sdi.UpdateUserAddressSdi;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value="getListAddress")
    public ResponseEntity<List<Address>> getListAddress(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.getListAddressByUser(token));
    }

    @PostMapping(value="setAddressDefault")
    public ResponseEntity<Boolean> setAddressDefault(@RequestBody SetAddressDefaultSdi sdi,
                                                     @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.setAddressDefault(token, sdi.getAddressId()));
    }

    @PostMapping(value="createUserAddress")
    public ResponseEntity<Address> createUserAddress(@RequestBody CreateUserAddressSdi sdi,
                                                     @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.createUserAddress(token, sdi));
    }

    @PostMapping(value="updateUserAddress")
    public ResponseEntity<Address> updateUserAddress(@RequestBody UpdateUserAddressSdi sdi,
                                                     @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(addressService.updateUserAddress(token, sdi));
    }
}
