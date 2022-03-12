package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.domain.User;
import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UserSdo {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String image;
    private String address;
    private String phoneNumber;
    private String token;
    private String shobbeName;


    public static UserSdo fromUser(User user) {
        return
            new UserSdo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage(),
                user.getAddress(),
                user.getPhoneNumber(),
                "",
                user.getShobbeName()
            );
    }
}
