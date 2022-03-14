package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Address;

public interface AddressRepoCustom {
    Address getAddressDefault(Long userId);
}
