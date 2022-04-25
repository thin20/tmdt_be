package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.ImageProduct;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ImageProductRepoCustom {
    List<ImageProduct> getListImageProductByProductId(Long productId);

    Boolean removeImageProductByProductIdAndPath(Long productId, String path) throws JsonProcessingException;
}
