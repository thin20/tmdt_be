package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.ImageProduct;

import java.util.List;

public interface ImageProductRepoCustom {
    List<ImageProduct> getListImageProductByProductId(Long productId);
}
