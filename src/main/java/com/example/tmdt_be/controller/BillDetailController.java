package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.BillDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="billDetail")
public class BillDetailController {
    @Autowired
    BillDetailService billDetailService;

    @GetMapping(value="countTotalProductSold")
    ResponseEntity<Long> countTotalProductSold(@RequestParam(value = "productId", required = true) Long productId) {
        return ResponseEntity.ok(billDetailService.countTotalProductSold(productId));
    }
}
