package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserAddressSdi {
    private String recipientName;
    private String recipientPhoneNumber;
    private String city;
    private String district;
    private String ward;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
}
