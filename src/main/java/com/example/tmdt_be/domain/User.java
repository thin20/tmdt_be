package com.example.tmdt_be.domain;

import com.example.tmdt_be.service.sdo.UserSdo;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="image", columnDefinition = "TEXT")
    private String image;

    @Column(name="created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(name="address")
    private String address;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="facebook_id")
    private String facebook_id;

    @Column(name="google_id")
    private String google_id;

    @Column(name="github_id")
    private String github_id;

    @Column(name="shobbe_name")
    private String shobbeName;

    public UserSdo toUserSdo() {
        UserSdo userSdo = new UserSdo();
        userSdo.setId(this.getId());
        userSdo.setEmail(this.getEmail());
        userSdo.setFirstName(this.getFirstName());
        userSdo.setLastName(this.getLastName());
        userSdo.setImage(this.getImage());
        userSdo.setAddress(this.getAddress());
        userSdo.setPhoneNumber(this.getPhoneNumber());
        userSdo.setShobbeName(this.getShobbeName());
        return userSdo;
    }
}
