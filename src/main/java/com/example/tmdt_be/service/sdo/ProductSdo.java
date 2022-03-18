package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.Category;
import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ProductSdo {
    private Long id;
    private String name;
    private Long categoryId;
    private Long userId;
    private Long quantity;
    private Long discount;
    private Double price;
    private String description;
    private String title;
    private Long numberOfStar;
    private String address;
    private String image;
    private Long isSell;
    private Long sold;
    private Long totalLiked;
    private Boolean canComment;
    private List<Category> categories;
    private Boolean isLiked;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
