package com.example.tmdt_be.service.sdo;

import lombok.*;

import java.util.Date;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ProductSdo {
    private Long Id;
    private String name;
    private Long categoryId;
    private Long userId;
    private Long quantity;
    private Long discount;
    private Double price;
    private String description;
    private String title;
    private Long numberOfStart;
    private String address;
    private String image;
    private Boolean isSell;
    private Long totalLiked;
    private Boolean isLiked;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
