package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepo extends JpaRepository<ImageProduct, Long>, ImageProductRepoCustom {
}
