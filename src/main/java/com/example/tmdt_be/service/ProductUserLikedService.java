package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdi.LikeProductSdi;

public interface ProductUserLikedService {
    Long getTotalLikedOfProduct(Long productId);

    Boolean isUserLikedProduct(LikeProductSdi sdi);

    Boolean likeProduct(LikeProductSdi sdi);

}
