package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.repository.ProductRepoCustom;
import com.example.tmdt_be.service.sdi.SearchProductSdi;
import com.example.tmdt_be.service.sdo.ProductSdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepoImpl implements ProductRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public List<ProductSdo> searchListProduct(SearchProductSdi sdi) {
        String keyword = sdi.getKeyword();
        Pageable pageable = sdi.getPageable();

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT ");
        sql.append(" id, ");
        sql.append(" `name`, ");
        sql.append(" price, ");
        sql.append(" description ");
        sql.append(" from Product ");

        if(!DataUtil.isNullOrEmpty(keyword)) {
            sql.append(" where Product.name like '%:keyword%' ");
            params.put("keyword", keyword);
        }

        Query query = em.createNativeQuery(sql.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();
        List<ProductSdo> result = DataUtil.getResultFromListObjects(queryResult, ProductSdo.class.getCanonicalName());

        return result;
    }

    @Override
    public Long countItemListProduct(SearchProductSdi sdi) {
        String keyword = sdi.getKeyword();
        Pageable pageable = sdi.getPageable();

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" select COUNT(1) ");
        sql.append(" from product ");

        if(!DataUtil.isNullOrEmpty(keyword)) {
            sql.append(" where product.name like '%:keyword%' ");
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.getSingleResult();

        return DataUtil.safeToLong(queryResult);
    }
}
