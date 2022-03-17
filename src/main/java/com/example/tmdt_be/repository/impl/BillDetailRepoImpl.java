package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.BillDetailRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class BillDetailRepoImpl implements BillDetailRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public Long countTotalProductSold(Long productId) {
        if (DataUtil.isNullOrZero(productId)) {
            return 0L;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" select SUM(tbl.quantity) ");
        sql.append(" from ( ");
        sql.append(" 	select id_product,  ");
        sql.append(" 	quantity  ");
        sql.append(" 	from bill_detail   ");
        sql.append(" 	where  ");
        sql.append(" 		(id_status = 2  ");
        sql.append(" 		or id_status = 3  ");
        sql.append(" 		or id_status = 4  ");
        sql.append(" 		or id_status = 5)  ");
        sql.append(" 		and id_product = :productId  ");
        params.put("productId", productId);
        sql.append(" ) tbl ");

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));
        Object queryResult = query.getSingleResult();
        Long count = DataUtil.safeToLong(queryResult);

        return count;
    }
}
