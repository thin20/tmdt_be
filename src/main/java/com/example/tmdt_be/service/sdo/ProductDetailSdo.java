package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.ImageProduct;
import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ProductDetailSdo {
    private List<ImageProduct> depicted;
    private ProductSdo productDetail;
}
