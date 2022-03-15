package com.example.tmdt_be.controller;

import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value="getListCategory")
    public ResponseEntity<List<Category>> getListCategory() {
        return ResponseEntity.ok(categoryService.getListCategory());
    }
}
