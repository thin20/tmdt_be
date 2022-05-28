package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.DashboardService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.sdo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DashboardServiceImpl implements DashboardService {
    private static UserService userService;

    @Autowired
    BillDetailRepo billDetailRepo;

    @Autowired
    ProductRepo productRepo;

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
        List<SalesRankingSdo> listSalesRanking = billDetailRepo.getListSaleRanking(sellerId, yearTime);

        List<SalesRankingSdo> listSalesRankingResult = new ArrayList<>();
        int length = listSalesRanking.size() < 7 ? listSalesRanking.size() : 7;
        for (int i = 0; i < length; i++) {
            listSalesRankingResult.add(listSalesRanking.get(i));
        }

        DataSalesDashboardSdo result = new DataSalesDashboardSdo();
        result.setSalesDashboard(listSalesDashboard);
        result.setSalesRanking(listSalesRanking);

        return result;
    }

    @Override
    public DataSellNumbersDashboardSdo getDataSellNumbersDashboard(String token, String yearTime) throws JsonProcessingException {
        Long sellerId = userService.getUserIdByBearerToken(token);

        List<SellNumberDashboardSdo> sellNumbersDashboardSdo = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            SellNumberDashboardSdo sellNumberDashboard = new SellNumberDashboardSdo();
            Long sold = billDetailRepo.getSellNumberDashboardByMonthAndYear(sellerId, DataUtil.safeToString(i), yearTime);

            sellNumberDashboard.setName("Tháng " + i);
            sellNumberDashboard.setSold(sold);

            sellNumbersDashboardSdo.add(sellNumberDashboard);
        }

        List<SellNumberRankingSdo> sellNumberRankings = billDetailRepo.getListSellNumberRanking(sellerId, yearTime);

        List<SellNumberRankingSdo> sellNumberRankingsResult = new ArrayList<>();
        int length = sellNumberRankings.size() < 7 ? sellNumberRankings.size() : 7;
        for (int i = 0; i < length; i++) {
            sellNumberRankingsResult.add(sellNumberRankings.get(i));
        }

        DataSellNumbersDashboardSdo result = new DataSellNumbersDashboardSdo();
        result.setSellNumberDashboard(sellNumbersDashboardSdo);
        result.setSellNumberRanking(sellNumberRankingsResult);

        return result;
    }

    @Override
    public List<DataVisitProductsDashboardSdo> getListTopVisitProduct(String token) throws JsonProcessingException {
        Long sellerId = userService.getUserIdByBearerToken(token);

        return productRepo.listTopVisitProduct(sellerId);
    }
}
