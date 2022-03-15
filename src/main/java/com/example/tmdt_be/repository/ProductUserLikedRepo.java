package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.ProductUserLiked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUserLikedRepo extends JpaRepository<ProductUserLiked, Long>, ProductUserLikedRepoCustom {
}
