package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.service.sdo.IdBillDetailSdo;

import java.util.List;

public interface BillDetailRepoCustom {
    Long countTotalProductSold(Long productId);

    Long countBillDetailOfUserAndProduct(Long userId, Long productId);

    List<IdBillDetailSdo> getListIdBillDetail(Long userId, Long purchaseType);

    BillDetail getBillById(Long billId, Long statusId);

    BillDetail getBillByUserAndProduct(Long userId, Long productId, Long statusId);
}
