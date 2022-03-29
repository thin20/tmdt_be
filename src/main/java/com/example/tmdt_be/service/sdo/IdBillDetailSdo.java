package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.Address;
import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class IdBillDetailSdo {
    private Long billId;
    private Long quantity;
    private Long sellerId;
    private Long addressId;
    private Long productId;
}