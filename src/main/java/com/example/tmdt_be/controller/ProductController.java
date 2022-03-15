package com.example.tmdt_be.controller;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
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
}
