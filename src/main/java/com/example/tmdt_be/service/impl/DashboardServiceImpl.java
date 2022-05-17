package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.service.DashboardService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.sdo.IdBillDetailSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DashboardServiceImpl implements DashboardService {
    private static UserService userService;

    @Autowired
    BillDetailRepo billDetailRepo;

    @Autowired
    DashboardServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Double getRevenueSellerByDate(String token, String dateTime) throws JsonProcessingException {
        Long sellerId = userService.getUserIdByBearerToken(token);
        AtomicReference<Double> totalRevenue = new AtomicReference<>(0.0);

        List<IdBillDetailSdo> idBillDetailSdo = billDetailRepo.getListIdBillDetailSellerByDate(sellerId, dateTime);
        idBillDetailSdo.forEach(item -> {
            totalRevenue.updateAndGet(v -> v + DataUtil.safeToDouble(item.getQuantity()) * DataUtil.safeToDouble(item.getPrice()));
        });

        return totalRevenue.get();
    }
}
