package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.service.sdo.IdBillDetailSdo;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillDetailRepoCustom {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<IdBillDetailSdo> getListIdBillDetail(Long userId, Long purchaseType);

    List<IdBillDetailSdo> getListIdBillDetailPagination(Long userId, Long purchaseType, Pageable pageable);

    Long countIdBillDetailPagination(Long userId, Long purchaseType);

    List<IdBillDetailSdo> getListIdBillDetailSellerPagination(Long sellerId, Long purchaseType, Pageable pageable);

    Long countIdBillDetailSellerPagination(Long userId, Long purchaseType);

    BillDetail getBillById(Long billId, Long statusId);

    BillDetail getBillByUserAndProduct(Long userId, Long productId, Long statusId);

    Boolean updateQuantityProductInCart(Long billId, Long quantity);

    Boolean deleteProductInCart(Long billId);

    List<IdBillDetailSdo> getListIdBillDetailSellerByDate (Long sellerId, String dateTime);
}
