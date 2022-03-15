package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.service.sdi.SearchProductSdi;

import java.util.List;

public interface ProductRepoCustom {
    List<Product> searchListProduct(SearchProductSdi sdi);

    Long countItemListProduct(SearchProductSdi sdi);
}
