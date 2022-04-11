package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Address;

import java.util.List;

public interface AddressRepoCustom {
    Address getAddressDefault(Long userId);

    List<Address> getListAddressByUser(Long userId);
}
