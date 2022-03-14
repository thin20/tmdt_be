package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.Address;

public interface AddressService {
    Address getAddressDefault(Long userId);
}
