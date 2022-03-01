package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, ProductRepoCustom {
}
