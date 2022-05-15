package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductSdi {
    private Long productId;
    private String productName;
    private Double price;
    private Long quantity;
    private Long discount;
    private String imagePath;
    private String description;
    private List<Long> fileIdRemove;
    private List<String> imagesPath;
}
