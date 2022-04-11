package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.service.sdi.CreateUserAddressSdi;
import com.example.tmdt_be.service.sdi.UpdateUserAddressSdi;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AddressService {
    Address getAddressDefault(Long userId);

    Address getAddressById(Long addressId);

    List<Address> getListAddressByUser(String token) throws JsonProcessingException;

    Boolean setAddressDefault(String token, Long addressId) throws JsonProcessingException;

    Address createUserAddress(String token, CreateUserAddressSdi sdi) throws JsonProcessingException;

    Address updateUserAddress(String token, UpdateUserAddressSdi sdi) throws JsonProcessingException;
}
