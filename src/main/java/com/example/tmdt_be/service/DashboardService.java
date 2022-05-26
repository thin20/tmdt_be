package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdo.DataSalesDashboardSdo;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface DashboardService {
    Double getRevenueSellerByDate(String token, String dateTime) throws JsonProcessingException;

    DataSalesDashboardSdo getDataSalesDashboard(String token, String yearTime) throws JsonProcessingException;
}
