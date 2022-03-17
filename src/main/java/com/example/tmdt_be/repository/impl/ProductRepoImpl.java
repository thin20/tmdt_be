package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.Const;
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
        Long categoryId = sdi.getCategoryId();
        String keyword = sdi.getKeyword();
        String sortType = sdi.getSortType();
        String orderType = sdi.getOrderType();
        Pageable pageable = sdi.getPageable();

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT ");
        sql.append(" id, ");
        sql.append(" `name`, ");
        sql.append(" id_category, ");
        sql.append(" id_user, ");
        sql.append(" quantity, ");
        sql.append(" discount, ");
        sql.append(" price, ");
        sql.append(" description, ");
        sql.append(" title, ");
        sql.append(" number_of_star, ");
        sql.append(" address, ");
        sql.append(" image, ");
        sql.append(" is_sell, ");
        sql.append(" '0L' as sold, ");
        sql.append(" '0L' as totalLiked, ");
        sql.append(" 'false' as isLiked, ");
        sql.append(" created_at, ");
        sql.append(" updated_at, ");
        sql.append(" deleted_at ");
        sql.append(" from product ");
        sql.append(" where 1 = 1 ");

        if(!DataUtil.isNullOrZero(categoryId)) {
            sql.append(" and id_category = :categoryId ");
            params.put("categoryId", categoryId);
        }

        if(!DataUtil.isNullOrEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            sql.append(" and `name` like :keyword ");
            params.put("keyword", keyword);
        }

        if (!DataUtil.isNullOrEmpty(sortType)) {
            if (sortType.equals(Const.SORT_TYPE.PRICE)) {
                sql.append(" order by (price - (1.0 * discount / 100.0) *  price)");
            } else {
                sql.append(" order by :sortType ");
                params.put("sortType", sortType);
            }
        }

        if (!DataUtil.isNullOrEmpty(orderType)) {
            if (orderType.equals(Const.ORDER_TYPE.ASC)) {
                sql.append(" asc ");
            } else {
                sql.append(" desc ");
            }
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
        Long categoryId = sdi.getCategoryId();
        String keyword = sdi.getKeyword();

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from product ");
        sql.append(" where 1 = 1 ");

        if(!DataUtil.isNullOrZero(categoryId)) {
            sql.append(" and id_category = :categoryId ");
            params.put("categoryId", categoryId);
        }

        if(!DataUtil.isNullOrEmpty(keyword)) {
            sql.append(" and `name` like :keyword ");
            params.put("keyword", keyword);
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.getSingleResult();

        return DataUtil.safeToLong(queryResult);
    }
}
