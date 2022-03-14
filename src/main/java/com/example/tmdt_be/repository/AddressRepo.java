package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long>, AddressRepoCustom {
}
