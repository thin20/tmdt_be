package com.example.tmdt_be.service;

public interface BillDetailService {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);
}
