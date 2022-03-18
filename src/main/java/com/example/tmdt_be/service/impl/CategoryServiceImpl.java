package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.repository.CategoryRepo;
import com.example.tmdt_be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<Category> getAllParentOfCategory(Long categoryId) {
        List<Category> listCategory = new ArrayList<>();
        return this.getCategoryById(categoryId, listCategory);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepo.getCategoryById(categoryId);
    }

    public List<Category> getCategoryById(Long categoryId, List<Category> listCategory) {
        Category category = categoryRepo.getCategoryById(categoryId);
        listCategory.add(category);

        Long parentCatId = category.getParentCategoryId();
        if (!DataUtil.isNullOrZero(parentCatId)) {
            getCategoryById(parentCatId, listCategory);
        }

        return listCategory;
    }
}
