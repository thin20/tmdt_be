package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.ProductService;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepo productRepo;

    @Override
    public Page<ProductSdo> searchListProduct(SearchProductSdi sdi) {
        Pageable pageable = sdi.getPageable();
        List<ProductSdo> listSdo = productRepo.searchListProduct(sdi);

        Long countItem = productRepo.countItemListProduct(sdi);
        return new PageImpl<>(listSdo, pageable, countItem);
    }
}
