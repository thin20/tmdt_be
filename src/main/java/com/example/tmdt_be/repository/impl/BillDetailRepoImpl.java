package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.repository.BillDetailRepoCustom;
import com.example.tmdt_be.service.sdo.IdBillDetailSdo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public Long countBillDetailOfUserAndProduct(Long userId, Long productId) {
        if (DataUtil.isNullOrZero(userId) | DataUtil.isNullOrZero(productId)) {
            return 0L;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from bill_detail ");
        sql.append(" where id_user = :userId ");
        params.put("userId", userId);
        sql.append(" and id_product = :productId ");
        params.put("productId", productId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.getSingleResult();

        return DataUtil.safeToLong(queryResult);
    }

    @Override
    public List<IdBillDetailSdo> getListIdBillDetail(Long userId, Long purchaseType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT bd.id as billId, ");
        sql.append(" bd.quantity as quantity, ");
        sql.append(" p.id_user as sellerId, ");
        sql.append(" bd.id_user as userId, ");
        sql.append(" bd.id_address as addressId, ");
        sql.append(" p.id as productId, ");
        sql.append(" bd.id_status as purchaseType ");
        sql.append(" from bill_detail bd ");
        sql.append(" join product p ");
        sql.append(" on bd.id_product = p.id ");
        sql.append(" and bd.id_status = :purchaseType ");
        params.put("purchaseType", purchaseType);
        sql.append(" and bd.id_user = :userId ");
        params.put("userId", userId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));
        List<Object[]> queryResult = query.getResultList();

        List<IdBillDetailSdo> result = DataUtil.getResultFromListObjects(queryResult, IdBillDetailSdo.class.getCanonicalName());

        return result;
    }

    @Override
    public List<IdBillDetailSdo> getListIdBillDetailPagination(Long userId, Long purchaseType, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT bd.id as billId, ");
        sql.append(" bd.quantity as quantity, ");
        sql.append(" p.id_user as sellerId, ");
        sql.append(" bd.id_user as userId, ");
        sql.append(" bd.id_address as addressId, ");
        sql.append(" p.id as productId, ");
        sql.append(" bd.id_status as purchaseType ");
        sql.append(" from bill_detail bd ");
        sql.append(" join product p ");
        sql.append(" on bd.id_product = p.id ");
        if (purchaseType != Const.PURCHASE_TYPE.ALL) {
            sql.append(" and bd.id_status = :purchaseType ");
            params.put("purchaseType", purchaseType);
        } else {
            sql.append(" and bd.id_status <> :purchaseType ");
            params.put("purchaseType", Const.PURCHASE_TYPE.ORDER);
        }
        sql.append(" and bd.id_user = :userId ");
        params.put("userId", userId);

        Query query = em.createNativeQuery(sql.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

        params.forEach((key, value) -> query.setParameter(key, value));
        List<Object[]> queryResult = query.getResultList();

        List<IdBillDetailSdo> result = DataUtil.getResultFromListObjects(queryResult, IdBillDetailSdo.class.getCanonicalName());

        return result;
    }

    @Override
    public Long countIdBillDetailPagination(Long userId, Long purchaseType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from bill_detail bd ");
        sql.append(" join product p ");
        sql.append(" on bd.id_product = p.id ");
        sql.append(" and bd.id_status = :purchaseType ");
        if (purchaseType != Const.PURCHASE_TYPE.ALL) {
            sql.append(" and bd.id_status = :purchaseType ");
            params.put("purchaseType", purchaseType);
        } else {
            sql.append(" and bd.id_status <> :purchaseType ");
            params.put("purchaseType", Const.PURCHASE_TYPE.ORDER);
        }
        sql.append(" and bd.id_user = :userId ");
        params.put("userId", userId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.getSingleResult();

        return DataUtil.safeToLong(queryResult);
    }

    @Override
    public List<IdBillDetailSdo> getListIdBillDetailSellerPagination(Long sellerId, Long purchaseType, Pageable pageable) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT bd.id as billId, ");
        sql.append(" bd.quantity as quantity, ");
        sql.append(" p.id_user as sellerId, ");
        sql.append(" bd.id_user as userId, ");
        sql.append(" bd.id_address as addressId, ");
        sql.append(" p.id as productId, ");
        sql.append(" bd.id_status as purchaseType ");
        sql.append(" from bill_detail bd ");
        sql.append(" join product p ");
        sql.append(" on bd.id_product = p.id ");
        if (purchaseType != Const.PURCHASE_TYPE.ALL) {
            sql.append(" and bd.id_status = :purchaseType ");
            params.put("purchaseType", purchaseType);
        } else {
            sql.append(" and bd.id_status <> :purchaseType1 ");
            params.put("purchaseType1", Const.PURCHASE_TYPE.ORDER);
            sql.append(" and bd.id_status <> :purchaseType2 ");
            params.put("purchaseType2", Const.PURCHASE_TYPE.CANCELED);
        }
        sql.append(" and p.id_user = :sellerId ");
        params.put("sellerId", sellerId);

        Query query = em.createNativeQuery(sql.toString());
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

        params.forEach((key, value) -> query.setParameter(key, value));
        List<Object[]> queryResult = query.getResultList();

        List<IdBillDetailSdo> result = DataUtil.getResultFromListObjects(queryResult, IdBillDetailSdo.class.getCanonicalName());

        return result;
    }

    @Override
    public Long countIdBillDetailSellerPagination(Long sellerId, Long purchaseType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from bill_detail bd ");
        sql.append(" join product p ");
        sql.append(" on bd.id_product = p.id ");
        if (purchaseType != Const.PURCHASE_TYPE.ALL) {
            sql.append(" and bd.id_status = :purchaseType ");
            params.put("purchaseType", purchaseType);
        } else {
            sql.append(" and bd.id_status <> :purchaseType1 ");
            params.put("purchaseType1", Const.PURCHASE_TYPE.ORDER);
            sql.append(" and bd.id_status <> :purchaseType2 ");
            params.put("purchaseType2", Const.PURCHASE_TYPE.CANCELED);
        }
        sql.append(" and p.id_user = :sellerId ");
        params.put("sellerId", sellerId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.getSingleResult();

        return DataUtil.safeToLong(queryResult);
    }

    @Override
    public BillDetail getBillById(Long billId, Long statusId) {
        if (DataUtil.isNullOrZero(billId) | DataUtil.isNullOrZero(statusId)) return null;

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT id, ");
        sql.append(" id_user, ");
        sql.append(" id_product, ");
        sql.append(" id_status, ");
        sql.append(" id_address, ");
        sql.append(" quantity, ");
        sql.append(" created_at, ");
        sql.append(" updated_at, ");
        sql.append(" deleted_at ");
        sql.append(" FROM bill_detail ");
        sql.append(" WHERE id = :billId ");
        params.put("billId", billId);
        sql.append(" and id_status = :statusId ");
        params.put("statusId", statusId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));
        List<Object[]> queryResult = query.getResultList();

        BillDetail result = new BillDetail();

        for (Object[] item : queryResult) {
            result = DataUtil.convertObjectsToClass(item, result);
            break;
        }

        return result;
    }

    @Override
    public BillDetail getBillByUserAndProduct(Long userId, Long productId, Long statusId) {
        if (DataUtil.isNullOrZero(userId) | DataUtil.isNullOrZero(productId) | DataUtil.isNullOrZero(statusId)) return null;

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT id, ");
        sql.append(" id_user, ");
        sql.append(" id_product, ");
        sql.append(" id_status, ");
        sql.append(" id_address, ");
        sql.append(" quantity, ");
        sql.append(" created_at, ");
        sql.append(" updated_at, ");
        sql.append(" deleted_at ");
        sql.append(" from bill_detail ");
        sql.append(" where id_product = :productId ");
        params.put("productId", productId);
        sql.append(" and id_user = :userId ");
        params.put("userId", userId);
        sql.append(" and id_status = :statusId ");
        params.put("statusId", statusId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();

        BillDetail result = new BillDetail();

        for (Object[] item : queryResult) {
            result = DataUtil.convertObjectsToClass(item, result);
            break;
        }

        return result;
    }

    @Override
    public Boolean updateQuantityProductInCart(Long billId, Long quantity) {
        if (DataUtil.isNullOrZero(billId) || DataUtil.isNullOrZero(quantity)) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" UPDATE bill_detail ");
        sql.append(" SET quantity = :quantity ");
        params.put("quantity", quantity);
        sql.append(" WHERE id = :billId ");
        params.put("billId", billId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.executeUpdate();

        return queryResult != null;
    }

    @Override
    public Boolean deleteProductInCart(Long billId) {
        if (DataUtil.isNullOrZero(billId)) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" DELETE FROM bill_detail ");
        sql.append(" WHERE id = :billId ");
        params.put("billId", billId);

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        Object queryResult = query.executeUpdate();

        return queryResult != null;
    }
}
