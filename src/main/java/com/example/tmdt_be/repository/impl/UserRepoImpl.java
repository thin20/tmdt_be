package com.example.tmdt_be.repository.impl;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.repository.UserRepoCustom;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class UserRepoImpl implements UserRepoCustom {
    @Autowired
    EntityManager em;

    public Optional<User> findById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT id, ");
        sql.append(" first_name, ");
        sql.append(" last_name, ");
        sql.append(" email, ");
        sql.append(" password, ");
        sql.append(" image, ");
        sql.append(" created_at, ");
        sql.append(" updated_at, ");
        sql.append(" address, ");
        sql.append(" phone_number, ");
        sql.append(" facebook_id, ");
        sql.append(" google_id, ");
        sql.append(" github_id, ");
        sql.append(" shobbe_name ");
        sql.append(" from users ");
        sql.append(" where 1 = 1 ");

        if (!DataUtil.isNullOrEmpty(id)) {
            sql.append(" and users.id = :id ");
            params.put("id", id);
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();

        return Optional.ofNullable(
                DataUtil.listNullOrEmpty(queryResult)
                        ? null
                        : DataUtil.convertObjectsToClass(queryResult.get(0), User.class.getCanonicalName())
        );
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        User user = new User();

        sql.append(" SELECT id, ");
        sql.append(" first_name, ");
        sql.append(" last_name, ");
        sql.append(" email, ");
        sql.append(" password, ");
        sql.append(" image, ");
        sql.append(" created_at, ");
        sql.append(" updated_at, ");
        sql.append(" address, ");
        sql.append(" phone_number, ");
        sql.append(" facebook_id, ");
        sql.append(" google_id, ");
        sql.append(" github_id, ");
        sql.append(" shobbe_name ");
        sql.append(" from users ");
        sql.append(" where 1 = 1 ");

        if (!DataUtil.isNullOrEmpty(phoneNumber)) {
            sql.append(" and phone_number = :phoneNumber ");
            params.put("phoneNumber", phoneNumber);
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();

        for (Object[] item : queryResult) {
            user =  DataUtil.convertObjectsToClass(item, user);
            break;
        }

        return user;
    }

    @Override
    public Boolean isExistsByPhoneNumber(String phoneNumber) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" SELECT COUNT(1) ");
        sql.append(" from users ");
        sql.append(" where 1 = 1 ");

        if (!DataUtil.isNullOrEmpty(phoneNumber)) {
            sql.append(" and phone_number = :phoneNumber ");
            params.put("phoneNumber", phoneNumber);
        }

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> queryResult = query.getResultList();

        if (DataUtil.listNullOrEmpty(queryResult)) return false;

        return DataUtil.safeToLong(queryResult.get(0)) > 0 ? true : false;
    }

    @Override
    public void createUser(CreateUserSdi sdi) {
        String firstName = sdi.getFirstName();
        String lastName = sdi.getLastName();
        String phoneNumber = sdi.getPhoneNumber();
        String password = sdi.getPassword();

        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append(" INSERT INTO users (first_name,  ");
        sql.append(" last_name,  ");
        sql.append(" password,  ");
        sql.append(" image,  ");
        sql.append(" phone_number) ");
        sql.append(" values ( ");

        if (!DataUtil.isNullOrEmpty(firstName)) {
            sql.append(" :firstName, ");
            params.put("firstName", firstName);
        } else {
            sql.append(" '', ");
        }

        if (!DataUtil.isNullOrEmpty(lastName)) {
            sql.append(" :lastName, ");
            params.put("lastName", lastName);
        } else {
            sql.append(" '', ");
        }

        if (!DataUtil.isNullOrEmpty(password)) {
            sql.append(" :password, ");
            params.put("password", password);
        } else {
            sql.append(" '', ");
        }

        sql.append(" :avatar, ");
        params.put("avatar", Const.AVATAR_DEFAULT);

        if (!DataUtil.isNullOrEmpty(phoneNumber)) {
            sql.append(" :phoneNumber ");
            params.put("phoneNumber", phoneNumber);
        } else {
            sql.append(" '' ");
        }

        sql.append(" ) ");

        Query query = em.createNativeQuery(sql.toString());

        params.forEach((key, value) -> query.setParameter(key, value));

        query.executeUpdate();
    }
}
