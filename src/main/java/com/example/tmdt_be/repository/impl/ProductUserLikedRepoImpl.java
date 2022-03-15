package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.ProductUserLiked;
import com.example.tmdt_be.repository.ProductUserLikedRepoCustom;
import com.example.tmdt_be.service.sdi.LikeProductSdi;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class ProductUserLikedRepoImpl implements ProductUserLikedRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public Long getTotalLikedOfProduct(Long productId) {
        if (DataUtil.isNullOrZero(productId)) {
            return 0L;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from product_user_liked ");
        sql.append(" where id_product = :productId ");
        params.put("productId", productId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));
        Object queryResult = query.getSingleResult();
        Long count = DataUtil.safeToLong(queryResult);

        return count;
    }

    @Override
    public Boolean isUserLikedProduct(LikeProductSdi sdi) {
        Long userId = sdi.getUserId();
        Long productId = sdi.getProductId();

        if (DataUtil.isNullOrZero(userId) | DataUtil.isNullOrZero(productId)) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from product_user_liked ");
        sql.append(" where id_user = :userId ");
        params.put("userId", userId);
        sql.append(" and id_product = :productId ");
        params.put("productId", productId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));
        Object queryResult = query.getSingleResult();
        Long count = DataUtil.safeToLong(queryResult);

        return count > 0;
    }

    @Override
    public void insert(LikeProductSdi sdi) {
        Long userId = sdi.getUserId();
        Long productId = sdi.getProductId();

        if (!DataUtil.isNullOrZero(userId) & !DataUtil.isNullOrZero(productId)) {

            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            sql.append(" INSERT INTO product_user_liked ( ");
            sql.append(" id_user, ");
            sql.append(" id_product) ");
            sql.append(" VALUES ( ");
            sql.append(" :userId, ");
            params.put("userId", userId);
            sql.append(" :productId) ");
            params.put("productId", productId);

            Query query = em.createNativeQuery(sql.toString());

            params.forEach((key, value) -> query.setParameter(key, value));
            query.executeUpdate();
        }
    }

    @Override
    public void delete(LikeProductSdi sdi) {
        Long userId = sdi.getUserId();
        Long productId = sdi.getProductId();

        if (!DataUtil.isNullOrZero(userId) & !DataUtil.isNullOrZero(productId)) {

            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            sql.append(" DELETE FROM product_user_liked ");
            sql.append("  where product_user_liked.id_user = :userId ");
            params.put("userId", userId);
            sql.append(" and product_user_liked.id_product = :productId ");
            params.put("productId", productId);

            Query query = em.createNativeQuery(sql.toString());

            params.forEach((key, value) -> query.setParameter(key, value));
            query.executeUpdate();
        }
    }
}
