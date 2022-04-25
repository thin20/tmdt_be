package com.example.tmdt_be.service.sdi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeImageProductSdi {
    private Long productId;
    private MultipartFile newImage;
    private String oldPathImage;
}
