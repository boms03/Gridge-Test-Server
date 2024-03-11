package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    private String phoneNumber;
    private String username;
    private String password;
    private String name;
    private Date birth;
    private boolean isTermsOfService;
    private boolean isDataBasedPolicy;
    private boolean isLocationBasedPolicy;

    public User toEntity() {
        return User.builder()
                .phoneNumber(this.phoneNumber)
                .password(this.password)
                .username(this.username)
                .name(this.name)
                .birth(this.birth)
                .build();
    }
}
