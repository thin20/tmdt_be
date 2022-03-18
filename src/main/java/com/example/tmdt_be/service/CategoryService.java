package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getListCategory();

    List<Category> getListCategoryParent();

    List<Category> getAllParentOfCategory(Long categoryId);

    Category getCategoryById(Long categoryId);
}
