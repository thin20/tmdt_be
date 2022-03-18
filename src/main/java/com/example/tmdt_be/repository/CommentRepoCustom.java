package com.example.tmdt_be.repository;

public interface CommentRepoCustom {
    Long countCommentOfUserAndProduct(Long userId, Long productId);
}
