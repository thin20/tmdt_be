package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductSdi {
    private Long currentUserId;
    private Long categoryId;
    private String keyword;
    private String sortType;
    private String orderType;
    private Pageable pageable;
}
