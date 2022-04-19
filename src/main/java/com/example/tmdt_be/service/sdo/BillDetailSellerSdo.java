package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.Address;
import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BillDetailSellerSdo {
    private Long billId;
    private Long purchaseType;
    private Long quantity;
    private Long sellerId;
    private String seller;
    private Address address;
    private UserSdo user;
    private ProductSdo product;
}
