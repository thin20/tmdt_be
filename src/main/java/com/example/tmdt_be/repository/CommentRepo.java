package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long>, CommentRepoCustom {
}
