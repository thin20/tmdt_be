package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.service.DashboardService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.sdo.DataSalesDashboardSdo;
import com.example.tmdt_be.service.sdo.IdBillDetailSdo;
import com.example.tmdt_be.service.sdo.SalesDashboardSdo;
import com.example.tmdt_be.service.sdo.SalesRankingSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public DataSalesDashboardSdo getDataSalesDashboard(String token, String yearTime) throws JsonProcessingException {
        Long sellerId = userService.getUserIdByBearerToken(token);

        // Get sales dashboard
        List<SalesDashboardSdo> listSalesDashboard = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AtomicReference<Double> revenue = new AtomicReference<>(0.0);
            List<IdBillDetailSdo> listIdBillDetail = billDetailRepo.getListIdBillDetailSellerByMonthAndYear(sellerId, DataUtil.safeToString(i), yearTime);
            listIdBillDetail.forEach(item -> {
                revenue.updateAndGet(v -> v + DataUtil.safeToDouble(item.getQuantity()) * DataUtil.safeToDouble(item.getPrice()));
            });

            SalesDashboardSdo saleDashboard = new SalesDashboardSdo();
            saleDashboard.setName("Tháng " + i);
            saleDashboard.setRevenue(revenue.get());

            listSalesDashboard.add(saleDashboard);
        }

        // Get sales ranking
        List<SalesRankingSdo> listSalesRanking = new ArrayList<>();

        DataSalesDashboardSdo result = new DataSalesDashboardSdo();
        result.setSalesDashboard(listSalesDashboard);
        result.setSalesRanking(listSalesRanking);

        return result;
    }
}
