package com.example.tmdt_be.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="product")
public class ProductUserLiked {
    @javax.persistence.Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="id_user")
    private Long userId;

    @Column(name="id_product")
    private Long productId;
}
