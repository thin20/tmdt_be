package com.example.tmdt_be.service.impl;

import com.example.tmdt_be.common.DataUtil;
import com.example.tmdt_be.domain.Address;
import com.example.tmdt_be.domain.User;
import com.example.tmdt_be.repository.UserRepo;
import com.example.tmdt_be.service.AddressService;
import com.example.tmdt_be.service.EncryptService;
import com.example.tmdt_be.service.TokenService;
import com.example.tmdt_be.service.UserService;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static EncryptService encryptService;
    private static TokenService tokenService;
    private static AddressService addressService;

    @Autowired
    UserRepo userRepository;

    @Autowired
    public UserServiceImpl(EncryptService encryptService, TokenService tokenService, AddressService addressService) {
        this.encryptService = encryptService;
        this.tokenService = tokenService;
        this.addressService = addressService;
    }

    @Override
    public UserSdo createUser(CreateUserSdi sdi) {
        String phoneNumber = sdi.getPhoneNumber();
        String password = sdi.getPassword();

        UserSdo userSdo = new UserSdo();

        if (DataUtil.isValidPhoneNumberVietNam(phoneNumber)) {
            throw new AppException("API-COM-PHONE001", "Số điện thoại không đúng định dạng!");
        }

        boolean isExists = userRepository.isExistsByPhoneNumber(phoneNumber);

        List<String> paramsError = new ArrayList<>();
        if (isExists) {
            paramsError.add(phoneNumber);
            throw new AppException("API-USR001", "Số điện thoại " + phoneNumber + " đã được sử dụng!", paramsError);
        }

        // Mã hóa password
        sdi.setPassword(encryptService.encryptPassword(password));

        userSdo.setFirstName(sdi.getFirstName());
        userSdo.setLastName(sdi.getLastName());
        userSdo.setPhoneNumber(sdi.getPhoneNumber());

        User user = sdi.toCreateUser();
        userRepository.save(user);

        return userSdo;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UserSdo login(LoginByPhoneNumberSdi sdi) throws JsonProcessingException {
        String phoneNumber = sdi.getPhoneNumber();
        String password = sdi.getPassword();

        if (DataUtil.isNullOrEmpty(phoneNumber)) {
            throw new AppException("API-USR002", "Số điện thoại bắt buộc nhập!");
        }

        if (DataUtil.isNullOrEmpty(password)) {
            throw new AppException("API-USR003", "Mật khẩu bắt buộc nhập!");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber);

        List<String> paramsError = new ArrayList<>();
        String token = "";
        String address = "";

        if (!DataUtil.isNullOrZero(user.getId())) {
            if (encryptService.checkPassword(password, user.getPassword())) {
                token = tokenService.generateToken(Long.toString(user.getId()));

                Address addressDefault = addressService.getAddressDefault(user.getId());
                if (!DataUtil.isNullOrZero(addressDefault.getId())) {
                    address = addressDefault.getAddress();
                }
            } else {
                throw new AppException("API-USR005", "Sai mật khẩu!");
            }
        } else {
            paramsError.add(phoneNumber);
            throw new AppException("API-USR004", "Số điện thoại " + phoneNumber + " chưa được đăng ký!", paramsError);
        }

        UserSdo userSdo = user.toUserSdo();
        userSdo.setToken(token);
        userSdo.setAddress(address);

        return userSdo;
    }
}
