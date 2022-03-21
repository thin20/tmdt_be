package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.Address;
import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class BillDetailSdo {
    private Long billId;
    private Long quantity;
    private String address;
    private ProductSdo product;
}
