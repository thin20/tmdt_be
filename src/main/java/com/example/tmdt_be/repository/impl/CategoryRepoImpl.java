package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Category;
import com.example.tmdt_be.repository.CategoryRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryRepoImpl implements CategoryRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public List<Category> getListCategory() {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT id, ");
        sql.append(" original_category_name, ");
        sql.append(" image, ");
        sql.append(" parent_category_id ");
        sql.append(" from category ");

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> queryResult = query.getResultList();
        List<Category> result = DataUtil.getResultFromListObjects(queryResult, Category.class.getCanonicalName());

        return result;
    }

    @Override
    public List<Category> getListCategoryParent() {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT id, ");
        sql.append(" original_category_name, ");
        sql.append(" image, ");
        sql.append(" parent_category_id ");
        sql.append(" from category ");
        sql.append(" where parent_category_id = 0 ");

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> queryResult = query.getResultList();
        List<Category> result = DataUtil.getResultFromListObjects(queryResult, Category.class.getCanonicalName());

        return result;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Category category = new Category();

        sql.append(" SELECT id, ");
        sql.append(" original_category_name, ");
        sql.append(" image, ");
        sql.append(" parent_category_id ");
        sql.append(" from category ");
        sql.append(" where 1 = 1 ");

        if (!DataUtil.isNullOrZero(categoryId)) {
            sql.append(" and id = :categoryId ");
            params.put("categoryId", categoryId);
        }

        Query query = em.createNativeQuery(sql.toString());
        params.forEach((key, value) -> query.setParameter(key, value));
        List<Object[]> queryResult = query.getResultList();

        for (Object[] item : queryResult) {
            category =  DataUtil.convertObjectsToClass(item, category);
            break;
        }

        return category;
    }
}
