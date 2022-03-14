package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.repository.AddressRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressRepoImpl implements AddressRepoCustom {
    @Autowired
    EntityManager em;

    @Override
    public Address getAddressDefault(Long userId) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Address address = new Address();

        sql.append(" SELECT id, ");
        sql.append(" id_user, ");
        sql.append(" recipient_name, ");
        sql.append(" recipient_phone_number, ");
        sql.append(" detail_address, ");
        sql.append(" ward, ");
        sql.append(" district, ");
        sql.append(" city, ");
        sql.append(" latitude, ");
        sql.append(" longitude, ");
        sql.append(" is_default, ");
        sql.append(" created_at, ");
        sql.append(" updated_at ");
        sql.append(" from address ");
        sql.append(" where is_default = 1 ");

        if (!DataUtil.isNullOrZero(userId)) {
            sql.append(" and id_user = :userId ");
            params.put("userId", userId);
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();

        for (Object[] item : queryResult) {
            address =  DataUtil.convertObjectsToClass(item, address);
            break;
        }

        return address;
    }

}
