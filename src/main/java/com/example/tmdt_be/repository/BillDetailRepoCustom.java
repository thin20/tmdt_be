package com.example.tmdt_be.repository;

public interface BillDetailRepoCustom {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);
}
