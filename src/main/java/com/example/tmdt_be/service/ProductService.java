package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductSdo> searchListProduct(SearchProductSdi sdi);
}
