package com.example.demo.src.admin.model;

import com.example.demo.common.Constant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
public class UserInfoRes {
    private Long id;

    private String email;

    private String phoneNumber;

    private String username;

    private String name;

    private String role;

    private boolean isOAuth;

    private String provider;

    protected String state;

    private Date birth;

    private LocalDateTime lastAgreedAt;

}
