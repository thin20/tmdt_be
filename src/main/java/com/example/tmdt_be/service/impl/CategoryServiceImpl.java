package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.repository.CategoryRepo;
import com.example.tmdt_be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public List<Category> getListCategory() {
        return categoryRepo.getListCategory();
    }

    @Override
    public List<Category> getListCategoryParent() {
        return categoryRepo.getListCategoryParent();
    }
}
