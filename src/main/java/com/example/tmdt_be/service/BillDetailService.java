package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.service.sdo.BillBySellerSdo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BillDetailService {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<BillBySellerSdo> getListBillBySeller(String token, Long purchaseType) throws JsonProcessingException;

    BillDetail getBillById(Long billId, Long statusId);

    BillDetail getBillByUserAndProduct(Long userId, Long productId);

    Boolean addToCart(String token, Long productId, Long quantity) throws JsonProcessingException;

    Boolean updateBillStatus(String token, Long billId, Long statusId) throws JsonProcessingException;
}
