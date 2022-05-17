package com.example.tmdt_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DashboardService {
    Double getRevenueSellerByDate(String token, String dateTime) throws JsonProcessingException;
}
