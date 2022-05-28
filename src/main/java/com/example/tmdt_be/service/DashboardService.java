package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdo.DataSalesDashboardSdo;
import com.example.tmdt_be.service.sdo.DataSellNumbersDashboardSdo;
import com.example.tmdt_be.service.sdo.DataVisitProductsDashboardSdo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DashboardService {
    Double getRevenueSellerByDate(String token, String dateTime) throws JsonProcessingException;

    DataSalesDashboardSdo getDataSalesDashboard(String token, String yearTime) throws JsonProcessingException;

    DataSellNumbersDashboardSdo getDataSellNumbersDashboard(String token, String yearTime) throws JsonProcessingException;

    List<DataVisitProductsDashboardSdo> getListTopVisitProduct(String token) throws JsonProcessingException;
}
