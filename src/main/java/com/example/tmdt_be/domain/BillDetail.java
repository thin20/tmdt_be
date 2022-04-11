package com.example.tmdt_be.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="bill_detail")
public class BillDetail {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_user")
    private Long userId;

    @Column(name="id_product")
    private Long productId;

    @Column(name="id_status")
    private Long statusId;

    @Column(name="id_address")
    private Long addressId;

    @Column(name="quantity")
    private Long quantity;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name="deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
}
