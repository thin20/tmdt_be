package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.ImageProduct;
import com.example.tmdt_be.repository.ImageProductRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageProductRepoImpl implements ImageProductRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public List<ImageProduct> getListImageProductByProductId(Long productId) {
        if (DataUtil.isNullOrZero(productId)) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT id, ");
        sql.append(" id_product, ");
        sql.append(" image ");
        sql.append(" from image_product ");
        sql.append(" where id_product = :productId ");
        params.put("productId", productId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();
        List<ImageProduct> result = DataUtil.getResultFromListObjects(queryResult, ImageProduct.class.getCanonicalName());

        return result;
    }
}
