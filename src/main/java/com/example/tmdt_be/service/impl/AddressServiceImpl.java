package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.repository.AddressRepo;
import com.example.tmdt_be.service.AddressService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.CreateUserAddressSdi;
import com.example.tmdt_be.service.sdi.UpdateUserAddressSdi;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    public static UserService userService;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    public AddressServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Address getAddressDefault(Long userId) {
        return addressRepo.getAddressDefault(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        Optional<Address> address = addressRepo.findById(addressId);
        return address.get();
    }

    @Override
    public List<Address> getListAddressByUser(String token) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);
        return addressRepo.getListAddressByUser(userId);
    }

    @Override
    public Boolean setAddressDefault(String token, Long addressId) throws JsonProcessingException {
        if (DataUtil.isNullOrZero(addressId)) {
            throw new AppException("API-ADR001", "Cập nhật địa chỉ mặc định thất bại!");
        }
        Long userId = userService.getUserIdByBearerToken(token);

        Address oldAddressDefault = addressRepo.getAddressDefault(userId);
        Address newAddressDefault = addressRepo.findById(addressId).orElseGet(() -> {
            throw new AppException("API-ADR001", "Cập nhật địa chỉ mặc định thất bại!");
        });

        if (userId != newAddressDefault.getUserId()) {
            throw new AppException("API-ADR002", "Không có quyền cập nhật mặc định cho địa chỉ!");
        }

        if (!DataUtil.isNullOrZero(oldAddressDefault.getId())) {
            oldAddressDefault.setIsDefault(0L);
            addressRepo.save(oldAddressDefault);
        }

        newAddressDefault.setIsDefault(1L);
        addressRepo.save(newAddressDefault);

        return true;
    }

    @Override
    public Address createUserAddress(String token, CreateUserAddressSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        String recipientName = sdi.getRecipientName();
        String recipientPhoneNumber = sdi.getRecipientPhoneNumber();
        String city = sdi.getCity();
        String district = sdi.getDistrict();
        String ward = sdi.getWard();
        String detailAddress = sdi.getDetailAddress();
        Double latitude = sdi.getLatitude();
        Double longitude = sdi.getLongitude();

        if (DataUtil.isNullOrEmpty(recipientName) |
                DataUtil.isNullOrEmpty(recipientPhoneNumber) |
                DataUtil.isNullOrEmpty(city) |
                DataUtil.isNullOrEmpty(district) |
                DataUtil.isNullOrEmpty(ward) |
                DataUtil.isNullOrEmpty(detailAddress) |
                DataUtil.isNullOrZero(latitude) |
                DataUtil.isNullOrZero(longitude)) {
            throw new AppException("API-ADR003", "Thêm mới địa chỉ thất bại!");
        }

        Address address = addressRepo.getAddressDefault(userId);
        Address newAddress = new Address();
        newAddress.setUserId(userId);
        newAddress.setRecipientName(recipientName);
        newAddress.setRecipientPhoneNumber(recipientPhoneNumber);
        newAddress.setCountry("Việt Nam");
        newAddress.setCity(city);
        newAddress.setDistrict(district);
        newAddress.setWard(ward);
        newAddress.setDetailAddress(detailAddress);
        newAddress.setLatitude(latitude);
        newAddress.setLongitude(longitude);
        newAddress.setCreatedAt(new Date());

        if (DataUtil.isNullOrZero(address.getId())) {
            newAddress.setIsDefault(1L);
        } else {
            newAddress.setIsDefault(0L);
        }

        return addressRepo.save(newAddress);
    }

    @Override
    public Address updateUserAddress(String token, UpdateUserAddressSdi sdi) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);

        Long id = sdi.getId();
        String recipientName = sdi.getRecipientName();
        String recipientPhoneNumber = sdi.getRecipientPhoneNumber();
        String city = sdi.getCity();
        String district = sdi.getDistrict();
        String ward = sdi.getWard();
        String detailAddress = sdi.getDetailAddress();
        Double latitude = sdi.getLatitude();
        Double longitude = sdi.getLongitude();

        if (DataUtil.isNullOrZero(id) |
                DataUtil.isNullOrEmpty(recipientName) |
                DataUtil.isNullOrEmpty(recipientPhoneNumber) |
                DataUtil.isNullOrEmpty(city) |
                DataUtil.isNullOrEmpty(district) |
                DataUtil.isNullOrEmpty(ward) |
                DataUtil.isNullOrEmpty(detailAddress) |
                DataUtil.isNullOrZero(latitude) |
                DataUtil.isNullOrZero(longitude)) {
            throw new AppException("API-ADR004", "Cập nhật địa chỉ thất bại!");
        }

        Address address = addressRepo.findById(id).orElseGet(() -> {
            throw new AppException("API-ADR005", "Địa chỉ chưa được đăng ký!");
        });

        if (address.getUserId() != userId) {
            throw new AppException("API-ADR007", "Không có quyền cập nhật địa chỉ!");
        }
        address.setRecipientName(recipientName);
        address.setRecipientPhoneNumber(recipientPhoneNumber);
        address.setCity(city);
        address.setDistrict(district);
        address.setWard(ward);
        address.setDetailAddress(detailAddress);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setUpdatedAt(new Date());

        return addressRepo.save(address);
    }

    @Override
    public Boolean deleteUserAddress(String token, Long addressId) throws JsonProcessingException {
        Long userId = userService.getUserIdByBearerToken(token);
        if (DataUtil.isNullOrZero(addressId)) {
            throw new AppException("API-ADR006", "Xóa địa chỉ thất bại!");
        }

        Address address = addressRepo.findById(addressId).orElseGet(() -> {
            throw new AppException("API-ADR009", "Địa chỉ không tồn tại!");
        });

        if (address.getUserId() != userId) {
            throw new AppException("API-ADR008", "Không có quyền xóa địa chỉ!");
        }

        if (address.getIsDefault() == 1) {
            throw new AppException("API-ADR010", "Không được xóa địa chỉ mặc định!");
        }

        addressRepo.delete(address);
        return true;
    }
}
