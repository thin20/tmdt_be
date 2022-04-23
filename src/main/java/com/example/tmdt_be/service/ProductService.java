package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdi.CreateProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductBySellerSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductDetailSdo;
import com.example.tmdt_be.service.sdo.ProductSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductSdo> searchListProduct(SearchProductSdi sdi);

    ProductDetailSdo getProductDetail(Long currentUserId, Long productId);

    ProductSdo getProductById(Long productId);

    Page<ProductSdo> searchListProductBySeller(String token,SearchProductBySellerSdi sdi) throws JsonProcessingException;

    ProductSdo createProduct(String token, CreateProductSdi sdi) throws JsonProcessingException;
}
