package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.repository.AddressRepo;
import com.example.tmdt_be.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepo addressRepo;

    @Override
    public Address getAddressDefault(Long userId) {
        return addressRepo.getAddressDefault(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        Optional<Address> address = addressRepo.findById(addressId);
        return address.get();
    }
}
