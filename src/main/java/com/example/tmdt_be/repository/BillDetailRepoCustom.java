package com.example.tmdt_be.repository;

import com.example.tmdt_be.service.sdo.IdBillDetailSdo;

import java.util.List;

public interface BillDetailRepoCustom {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<IdBillDetailSdo> getListIdBillDetail(Long userId, Long purchaseType);
}
