package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.ProductService;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import lombok.extern.slf4j.Slf4j;
import me.coong.web.response.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping(value = "/search-list")
    public ResponseEntity<PagedResponse<ProductSdo>> searchProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                                                    @PageableDefault Pageable pageable) {
        SearchProductSdi sdi = new SearchProductSdi();
        sdi.setKeyword(keyword);
        sdi.setPageable(pageable);

        Page<ProductSdo> result = productService.searchListProduct(sdi);

        return ResponseEntity.ok(PagedResponse.builder().page(result).build());
    }
}
