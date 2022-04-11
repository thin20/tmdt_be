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
@Table(name="address")
public class Address {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_user")
    private Long userId;

    @Column(name="recipient_name")
    private String recipientName;

    @Column(name="recipient_phone_number")
    private String recipientPhoneNumber;

    @Column(name="detail_address")
    private String detailAddress;

    @Column(name="ward")
    private String ward;

    @Column(name="district")
    private String district;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="latitude")
    private Double latitude;

    @Column(name="longitude")
    private Double longitude;

    @Column(name="is_default")
    private Long isDefault;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public String getAddress() {
        return this.getDetailAddress() + ", " + this.getWard() + ", " + this.getDistrict() + ", " + this.getCity();
    }
}
