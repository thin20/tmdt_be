package com.example.tmdt_be.domain;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.service.sdo.ProductSdo;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="product")
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="name")
    private String name;

    @Column(name="id_category")
    private Long categoryId;

    @Column(name="id_user")
    private Long userId;

    @Column(name="quantity")
    private Long quantity;

    @Column(name="discount")
    private Long discount;

    @Column(name="price")
    private Double price;

    @Column(name="description")
    private String description;

    @Column(name="title")
    private String title;

    @Column(name="number_of_star")
    private Long numberOfStart;

    @Column(name="address")
    private String address;

    @Column(name="image")
    private String image;

    @Column(name="is_sell")
    private Integer isSell;

    @Column(name="created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(name="deleted_at")
    @Temporal(TemporalType.DATE)
    private Date deletedAt;

    public ProductSdo toProductSdo() {
        ProductSdo productSdo = new ProductSdo();
        productSdo.setId(this.getId());
        productSdo.setName(this.getName());
        productSdo.setCategoryId(this.getCategoryId());
        productSdo.setUserId(this.getUserId());
        productSdo.setQuantity(this.getQuantity());
        productSdo.setDiscount(this.getDiscount());
        productSdo.setPrice(this.getPrice());
        productSdo.setDescription(this.getDescription());
        productSdo.setTitle(this.getTitle());
        productSdo.setNumberOfStart(this.getNumberOfStart());
        productSdo.setAddress(this.getAddress());
        productSdo.setImage(this.getImage());
        if (!DataUtil.isNullOrZero(this.getIsSell())) {
            productSdo.setIsSell(true);
        } else {
            productSdo.setIsSell(false);
        }
        productSdo.setCreatedAt(this.getCreatedAt());
        productSdo.setUpdatedAt(this.getUpdatedAt());
        productSdo.setDeletedAt(this.getDeletedAt());

        return productSdo;
    }
}
