package com.example.tmdt_be.repository;

import com.example.tmdt_be.domain.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long>, BillDetailRepoCustom {
    @Query(
            value = "SELECT * FROM bill_detail a WHERE a.id_user = :userId AND a.id_product = :productId AND a.id_status = :statusId",
            nativeQuery = true
    )
    Optional<BillDetail> findAllByUserIdAndProductId(Long userId, Long productId, Long statusId);

    @Query(
           value = "SELECT * FROM bill_detail a WHERE a.id = :billId and a.id_status = :statusId",
           nativeQuery = true
    )
    Optional<BillDetail> findBillByIdAndStatus(Long billId, Long statusId);
}
