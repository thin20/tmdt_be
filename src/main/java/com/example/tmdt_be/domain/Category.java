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
@Table(name="category")
public class Category {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="original_category_name")
    private String original_category_name;

    @Column(name="image")
    private String image;

    @Column(name="parent_category_id")
    private Long parentCategoryId;
}
