package com.example.tmdt_be.service.sdo;

import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CommentSdo {
    private Long id;
    private Long userId;
    private Long productId;
    private Long billId;
    private String comment;
    private Long star;
    private String createdAt;
    private String updatedAt;
}
