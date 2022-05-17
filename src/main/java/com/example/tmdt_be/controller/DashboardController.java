package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.DashboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value="dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    @GetMapping(value = "getRevenueSellerByDate")
    public ResponseEntity<Double> getRevenueSellerByDate(@RequestParam(value = "dateTime", required = false) String dateTime,
                                                         @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return ResponseEntity.ok(dashboardService.getRevenueSellerByDate(token, dateTime));
    }
}
