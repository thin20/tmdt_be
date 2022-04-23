package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.ImageProduct;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

public interface ImageProductService {
    Optional<ImageProduct> getImageProductById(Long id);

    List<ImageProduct> getListImageProductByProductId(Long productId);

    Boolean saveImageProduct(Long productId, String path) throws JsonProcessingException;
}
