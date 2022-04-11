package com.example.tmdt_be.service;

import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.service.sdi.BuyProductsSdi;
import com.example.tmdt_be.service.sdi.DeleteProductsInCartSdi;
import com.example.tmdt_be.service.sdo.BillBySellerSdo;
import com.example.tmdt_be.service.sdo.BillDetailSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillDetailService {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<BillBySellerSdo> getListBillBySeller(String token, Long purchaseType) throws JsonProcessingException;

    Page<BillDetailSdo> getListBillByUserAndStatus(String token, Long purchaseType, Pageable pageable) throws JsonProcessingException;

    BillDetail getBillById(Long billId, Long statusId);

    BillDetail getBillByUserAndProduct(Long userId, Long productId);

    Boolean addToCart(String token, Long productId, Long quantity) throws JsonProcessingException;

    Boolean updateBillStatus(String token, Long billId, Long statusId) throws JsonProcessingException;

    Boolean updateQuantityProductInCart(String token, Long billId, Long quantity) throws JsonProcessingException;

    Boolean deleteProductInCart(String token, Long billId) throws JsonProcessingException;

    Boolean deleteProductsInCart(String token, DeleteProductsInCartSdi sdi) throws JsonProcessingException;

    Boolean buyProducts(String token, BuyProductsSdi sdi) throws JsonProcessingException;
}
