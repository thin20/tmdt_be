package com.example.tmdt_be.service.sdo;

import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class SellNumberRankingSdo {
    private String name;
    private Long sold;
    private Long inventory;
}
