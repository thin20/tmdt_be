package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.ProductUserLikedService;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="productUserLiked")
public class ProductUserLikedController {
    @Autowired
    ProductUserLikedService productUserLikedService;

    @GetMapping(value="getTotalLikedOfProduct")
    public ResponseEntity<Long> getTotalLikedOfProduct(@RequestParam(value="productId", required = true) Long productId) {
        return ResponseEntity.ok(productUserLikedService.getTotalLikedOfProduct(productId));
    }

    @GetMapping(value="isUserLikedProduct")
    public ResponseEntity<Boolean> isUserLikedProduct(@RequestParam(value="userId", required = true) Long userId,
                                                      @RequestParam(value="productId", required = true) Long productId) {
        LikeProductSdi sdi = new LikeProductSdi();
        sdi.setUserId(userId);
        sdi.setProductId(productId);
        return ResponseEntity.ok(productUserLikedService.isUserLikedProduct(sdi));
    }

    @PostMapping(value="likeProduct")
    public ResponseEntity<Boolean> likeProduct(@Valid @RequestBody LikeProductSdi sdi) {
        return ResponseEntity.ok(productUserLikedService.likeProduct(sdi));
    }
}
