package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.CommentRepo;
import com.example.tmdt_be.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepo commentRepo;

    @Override
    public Long countCommentOfUserAndProduct(Long userId, Long productId) {
        if (DataUtil.isNullOrZero(userId) | DataUtil.isNullOrZero(productId)) {
            return 0L;
        }

        return commentRepo.countCommentOfUserAndProduct(userId, productId);
    }
}
