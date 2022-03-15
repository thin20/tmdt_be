package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.ProductUserLikedRepo;
import com.example.tmdt_be.service.ProductUserLikedService;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductUserLikedServiceImpl implements ProductUserLikedService {
    @Autowired
    ProductUserLikedRepo productUserLikedRepo;

    @Override
    public Long getTotalLikedOfProduct(Long productId) {
        return productUserLikedRepo.getTotalLikedOfProduct(productId);
    }

    @Override
    public Boolean isUserLikedProduct(LikeProductSdi sdi) {
        return productUserLikedRepo.isUserLikedProduct(sdi);
    }

    @Override
    public Boolean likeProduct(LikeProductSdi sdi) {
        if (DataUtil.isNullOrZero(sdi.getUserId()) & DataUtil.isNullOrZero(sdi.getProductId())) {
            return false;
        }

        Boolean isLiked = productUserLikedRepo.isUserLikedProduct(sdi);
        if (isLiked) {
            productUserLikedRepo.delete(sdi);
        } else {
            productUserLikedRepo.insert(sdi);
        }
        return true;
    }
}
