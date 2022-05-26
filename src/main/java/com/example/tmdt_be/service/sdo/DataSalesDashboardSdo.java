package com.example.tmdt_be.service.sdo;

import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class DataSalesDashboardSdo {
    private List<SalesDashboardSdo> salesDashboard;
    private List<SalesRankingSdo> salesRanking;
}
