package com.example.tmdt_be.repository;

import com.example.tmdt_be.service.sdi.LikeProductSdi;

public interface ProductUserLikedRepoCustom {
    Long getTotalLikedOfProduct(Long productId);

    Boolean isUserLikedProduct(LikeProductSdi sdi);

    void insert(LikeProductSdi sdi);

    void delete(LikeProductSdi sdi);
}
