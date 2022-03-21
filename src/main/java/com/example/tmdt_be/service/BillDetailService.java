package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdo.BillBySellerSdo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BillDetailService {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<BillBySellerSdo> getListBillBySeller(String token, Long purchaseType) throws JsonProcessingException;
}
