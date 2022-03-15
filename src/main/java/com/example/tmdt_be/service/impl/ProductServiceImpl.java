package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.ProductService;
import com.example.tmdt_be.service.ProductUserLikedService;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static ProductUserLikedService productUserLikedService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductUserLikedService productUserLikedService) {
        this.productUserLikedService = productUserLikedService;
    }

    @Override
    public Page<ProductSdo> searchListProduct(SearchProductSdi sdi) {
        Pageable pageable = sdi.getPageable();
        List<Product> listProduct = productRepo.searchListProduct(sdi);

        List<ProductSdo> listSdo = new ArrayList<>();
        for (Product product : listProduct) {
            ProductSdo productSdo = product.toProductSdo();

            Long currentUserId = sdi.getCurrentUserId();
            Long productId = product.getId();

            Long totalLikedOfProduct = productUserLikedService.getTotalLikedOfProduct(productId);
            productSdo.setTotalLiked(totalLikedOfProduct);

            Boolean isLiked = false;
            if (!DataUtil.isNullOrZero(currentUserId)) {
                LikeProductSdi lpSdi = new LikeProductSdi();
                lpSdi.setUserId(currentUserId);
                lpSdi.setProductId(productId);
                isLiked = productUserLikedService.isUserLikedProduct(lpSdi);
            }
            productSdo.setIsLiked(isLiked);

            listSdo.add(productSdo);
        }

        Long countItem = productRepo.countItemListProduct(sdi);

        return new PageImpl<>(listSdo, pageable, countItem);
    }
}
