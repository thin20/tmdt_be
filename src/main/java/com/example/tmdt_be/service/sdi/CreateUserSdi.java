package com.example.tmdt_be.service.sdi;

import com.example.tmdt_be.common.Const;
import com.example.tmdt_be.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserSdi {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;

    public User toCreateUser() {
        User user = new User();
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setPhoneNumber(this.getPhoneNumber());
        user.setPassword(this.getPassword());
        user.setImage(Const.AVATAR_DEFAULT);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return user;
    }
}
