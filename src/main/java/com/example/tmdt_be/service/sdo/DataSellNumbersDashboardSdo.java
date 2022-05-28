package com.example.tmdt_be.service.sdo;

import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class DataSellNumbersDashboardSdo {
    private List<SellNumberDashboardSdo> sellNumberDashboard;
    private List<SellNumberRankingSdo> sellNumberRanking;
}
