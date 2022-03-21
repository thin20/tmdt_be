package com.example.tmdt_be.service.sdo;

import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BillBySellerSdo {
    private Long sellerId;
    private String sellerName;
    private List<BillDetailSdo> bills;
}
