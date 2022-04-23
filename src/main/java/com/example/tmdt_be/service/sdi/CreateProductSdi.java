package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductSdi {
    private Long categoryId;
    private String productName;
    private Long quantity;
    private Double price;
    private Long discount;
    private List<MultipartFile> images;
    private String description;
}
