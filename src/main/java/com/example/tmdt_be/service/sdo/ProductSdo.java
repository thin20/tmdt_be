package com.example.tmdt_be.service.sdo;

import lombok.Data;

@Data
public class ProductSdo {
    private Long Id;
    private String name;
    private Double price;
    private String description;
}
