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
import com.example.tmdt_be.service.sdi.ChangePasswordSdi;
import com.example.tmdt_be.service.sdi.CreateUserSdi;
import com.example.tmdt_be.service.sdi.LoginByPhoneNumberSdi;
import com.example.tmdt_be.service.sdo.UserSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public UserSdo loginByToken(String token) throws JsonProcessingException {
        UserSdo userSdo = new UserSdo();
        if (DataUtil.isNullOrEmpty(token)) {
            throw new AppException("API-USR006", "Token không được trống!");
        }

        String address = "";
        if (!tokenService.isTokenExpired(token)) {
            Optional<String> id = tokenService.getSubFromToken(token);
            if (id.isPresent()) {
                Long idSdi = DataUtil.safeToLong(id.get());
                User user = userRepository.getOne(idSdi);

                if (!DataUtil.isNullOrZero(user.getId())) {
                    userSdo = user.toUserSdo();

                    Address addressDefault = addressService.getAddressDefault(user.getId());
                    if (!DataUtil.isNullOrZero(addressDefault.getId())) {
                        address = addressDefault.getAddress();
                    }
                } else {
                    throw new AppException("API-USR008", "User không tồn tại!");
                }
            }
        } else {
            throw new AppException("API-USR007", "Token hết hạn!");
        }

        userSdo.setToken(token);
        userSdo.setAddress(address);
        return userSdo;
    }

    @Override
    public UserSdo findById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.get().toUserSdo();
    }

    @Override
    public Long getUserIdByBearerToken(String token) throws JsonProcessingException{
        Long userId = null;
        if (!DataUtil.isNullOrEmpty(token)) {
            token = token.split(" ")[1];
        }
        UserSdo userSdo = this.loginByToken(token);
        if (!DataUtil.isNullOrZero(userSdo.getId())) {
            userId = userSdo.getId();
        } else {
            throw new AppException("API-USR008", "User không tồn tại!");
        }

        return userId;
    }

    @Override
    public UserSdo getUserByBearerToken(String token) throws JsonProcessingException {
        if (!DataUtil.isNullOrEmpty(token)) {
            token = token.split(" ")[1];
        }
        UserSdo userSdo = this.loginByToken(token);
        if (!DataUtil.isNullOrZero(userSdo.getId())) {
        } else {
            throw new AppException("API-USR008", "User không tồn tại!");
        }
        return userSdo;
    }

    @Override
    public Boolean changePassword(String token, ChangePasswordSdi sdi) throws JsonProcessingException {
        String oldPassword = sdi.getOldPassword();
        String newPassword = sdi.getNewPassword();
        Long userId = this.getUserIdByBearerToken(token);

        User user = userRepository.findById(userId).get();

        if (encryptService.checkPassword(oldPassword, user.getPassword())) {
            if (oldPassword.equals(newPassword)) {
                throw new AppException("API-USR009", "Mật khẩu mới phải khác mật khẩu cũ!");
            }
            user.setPassword(encryptService.encryptPassword(newPassword));
            userRepository.save(user);
        } else {
            throw new AppException("API-USR005", "Sai mật khẩu!");
        }
        return true;
    }
}
