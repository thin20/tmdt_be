package com.example.tmdt_be.controller;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.service.ProductService;
import com.example.tmdt_be.service.sdi.CreateProductSdi;
import com.example.tmdt_be.service.sdi.SearchProductBySellerSdi;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductDetailSdo;
import com.example.tmdt_be.service.sdo.ProductSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.coong.web.response.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping(value = "/search-list")
    public ResponseEntity<PagedResponse<ProductSdo>> searchListProduct(@RequestParam(value = "currentUserId", required = false) Long currentUserId,
                                                                    @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                    @RequestParam(value = "keyword", required = false) String keyword,
                                                                    @RequestParam(value = "sortType", required = false) String sortType,
                                                                    @RequestParam(value = "orderType", required = false) String orderType,
                                                                    @PageableDefault Pageable pageable) {
        SearchProductSdi sdi = new SearchProductSdi();
        sdi.setCurrentUserId(currentUserId);
        sdi.setCategoryId(categoryId);
        sdi.setKeyword(keyword);
        if (DataUtil.isNullOrEmpty(sortType)) {
            sdi.setSortType(Const.SORT_TYPE.POPULAR);
        } else {
            sdi.setSortType(sortType);
        }
        if (DataUtil.isNullOrEmpty(orderType)) {
            sdi.setOrderType(Const.ORDER_TYPE.DESC);
        } else {
            sdi.setOrderType(orderType);
        }
        sdi.setPageable(pageable);

        Page<ProductSdo> result = productService.searchListProduct(sdi);

        return ResponseEntity.ok(PagedResponse.builder().page(result).build());
    }

    @GetMapping(value="getProductDetail")
    public ResponseEntity<ProductDetailSdo> getProductDetail(@RequestParam(value = "currentUserId", required = false) Long currentUserId,
                                                             @RequestParam(value = "productId", required = true) Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(currentUserId, productId));
    }

    @GetMapping(value="getProductById")
    public ResponseEntity<ProductSdo> getProductById(@RequestParam(value = "productId", required = true) Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping(value="getListProductBySeller")
    public ResponseEntity<PagedResponse<ProductSdo>> getListProductBySeller(@RequestParam(value="categoryId", required = false) Long categoryId,
                                                                   @RequestParam(value="keyword", required = false) String keyword,
                                                                   @RequestHeader("Authorization") String token,
                                                                   @PageableDefault Pageable pageable) throws JsonProcessingException {
        SearchProductBySellerSdi sdi = new SearchProductBySellerSdi();
        sdi.setCategoryId(categoryId);
        sdi.setKeyword(keyword);
        sdi.setPageable(pageable);

        Page<ProductSdo> result = productService.searchListProductBySeller(token, sdi);
        return ResponseEntity.ok(PagedResponse.builder().page(result).build());
    }

    @PostMapping(value="createProduct")
    public ResponseEntity<ProductSdo> createProduct(@Valid @RequestBody CreateProductSdi sdi,
                                                    @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                    @RequestHeader("Authorization") String token) throws JsonProcessingException {
        sdi.setImages(images);
        return ResponseEntity.ok(productService.createProduct(token, sdi));
    }
}
