package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.domain.BillDetail;
import com.example.tmdt_be.domain.Product;
import com.example.tmdt_be.repository.BillDetailRepo;
import com.example.tmdt_be.repository.ProductRepo;
import com.example.tmdt_be.service.AddressService;
import com.example.tmdt_be.service.BillDetailService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BillDetailServiceImpl implements BillDetailService {
    private static UserService userService;
    private static AddressService addressService;

    @Autowired
    BillDetailRepo billDetailRepo;

    @Autowired
    ProductRepo productRepo;

    public BillDetailServiceImpl(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @Override
    public Long countTotalProductSold(Long productId) {
        return billDetailRepo.countTotalProductSold(productId);
    }

    @Override
    public Long countBillDetailOfUserAndProduct(Long userId, Long productId) {
        return billDetailRepo.countBillDetailOfUserAndProduct(userId, productId);
    }

    @Override
    public List<BillBySellerSdo> getListBillBySeller(String token,Long purchaseType) throws JsonProcessingException {
        Long userId = null;
        if (!DataUtil.isNullOrEmpty(token)) {
            token = token.split(" ")[1];
        }
        UserSdo userSdo = userService.loginByToken(token);
        if (!DataUtil.isNullOrZero(userSdo.getId())) {
            userId = userSdo.getId();
        } else {
            throw new AppException("API-USR008", "User không tồn tại!");
        }

        List<IdBillDetailSdo> listIdBillDetail = billDetailRepo.getListIdBillDetail(userId, purchaseType);
        HashMap<Long, List<BillDetailSdo>> hashMap = new HashMap<Long, List<BillDetailSdo>>();
        for (IdBillDetailSdo item : listIdBillDetail) {
            BillDetailSdo billDetailSdo = new BillDetailSdo();

            billDetailSdo.setBillId(item.getBillId());

            billDetailSdo.setQuantity(item.getQuantity());

            if (!DataUtil.isNullOrZero(item.getAddressId())) {
                Address address = addressService.getAddressById(item.getAddressId());
                billDetailSdo.setAddress(address.getAddress());
            }

            if (!DataUtil.isNullOrZero(item.getProductId())) {
                ProductSdo productSdo = productRepo.getProductById(item.getProductId());
                billDetailSdo.setProduct(productSdo);
            }

            if (!hashMap.containsKey(item.getSellerId())) {
                List<BillDetailSdo> list = new ArrayList<BillDetailSdo>();

                list.add(billDetailSdo);

                hashMap.put(item.getSellerId(), list);
            } else {
                hashMap.get(item.getSellerId()).add(billDetailSdo);
            }
        }

        List<BillBySellerSdo> listBillBySeller = new ArrayList<BillBySellerSdo>();

        for(Map.Entry<Long, List<BillDetailSdo>> entry : hashMap.entrySet()) {

            Long sellerId = entry.getKey();
            List<BillDetailSdo> value = entry.getValue();

            if (DataUtil.isNullOrZero(sellerId)) break;

            UserSdo user = userService.findById(sellerId);

            BillBySellerSdo billBySellerSdo = new BillBySellerSdo();
            billBySellerSdo.setSellerId(sellerId);
            billBySellerSdo.setSellerName(user.getFullName());
            billBySellerSdo.setBills(value);

            listBillBySeller.add(billBySellerSdo);
        }

        return listBillBySeller;
    }

    @Override
    public Boolean createBillDetail(String token, Long productId, Long quantity) throws JsonProcessingException {
        Long userId = null;
        if (!DataUtil.isNullOrEmpty(token)) {
            token = token.split(" ")[1];
        }
        UserSdo userSdo = userService.loginByToken(token);
        if (!DataUtil.isNullOrZero(userSdo.getId())) {
            userId = userSdo.getId();
        } else {
            throw new AppException("API-USR008", "User không tồn tại!");
        }

        BillDetail billDetail = new BillDetail();
        billDetail.setUserId(userId);

        ProductSdo productSdo = productRepo.getProductById(productId);
        if (!DataUtil.isNullOrZero(productSdo.getId())) {
            billDetail.setProductId(productId);
        } else {
            throw new AppException("API-PRD001", "Sản phẩm không tồn tại!");
        }

        quantity = !DataUtil.isNullOrZero(quantity) ? quantity : 1;
        billDetail.setQuantity(quantity);

        billDetail.setCreatedAt(new Date());

        BillDetail bd = billDetailRepo.save(billDetail);
        return !DataUtil.isNullOrZero(bd.getId());
    }

    @Override
    public Boolean updateBillDetail(String token, Long productId, Long quantity) throws JsonProcessingException {
        return null;
    }
}
