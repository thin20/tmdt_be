package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, UserRepoCustom {
}
