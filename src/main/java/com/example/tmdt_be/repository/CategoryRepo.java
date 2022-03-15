package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long>, CategoryRepoCustom {
}