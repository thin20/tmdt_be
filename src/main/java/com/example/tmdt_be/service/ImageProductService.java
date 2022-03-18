package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.ImageProduct;

import java.util.List;
import java.util.Optional;

public interface ImageProductService {
    Optional<ImageProduct> getImageProductById(Long id);

    List<ImageProduct> getListImageProductByProductId(Long productId);
}
