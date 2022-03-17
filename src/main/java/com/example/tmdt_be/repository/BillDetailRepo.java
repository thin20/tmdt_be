package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long>, BillDetailRepoCustom {
}
