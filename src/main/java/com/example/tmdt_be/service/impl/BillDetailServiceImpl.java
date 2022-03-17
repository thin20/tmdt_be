package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillDetailServiceImpl implements BillDetailService {
    @Autowired
    BillDetailRepo billDetailRepo;

    @Override
    public Long countTotalProductSold(Long productId) {
        return billDetailRepo.countTotalProductSold(productId);
    }
}
