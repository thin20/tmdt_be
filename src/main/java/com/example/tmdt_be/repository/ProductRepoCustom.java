package com.example.tmdt_be.repository;

import com.example.tmdt_be.service.sdi.SearchProductBySellerSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;

import java.util.List;

public interface ProductRepoCustom {
    List<ProductSdo> searchListProduct(SearchProductSdi sdi);

    Long countItemListProduct(SearchProductSdi sdi);

    ProductSdo getProductById(Long productId);

    List<ProductSdo> searchListProductBySeller(Long userId, SearchProductBySellerSdi sdi);

    Long countItemListProductBySeller(Long userId, SearchProductBySellerSdi sdi);
}
