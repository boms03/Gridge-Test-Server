package com.example.demo.common;

public class Constant {
    public enum SocialLoginType{
        GOOGLE,
        KAKAO,
        NAVER
    }

    public enum State {
        ACTIVE,
        INACTIVE

    }

    public enum UserState {
        ACTIVE,
        DORMANT,
        WITHDRAW,
        BANNED,
        RENEW
    }

    public enum BoardState {
        VISIBLE,

        INVISIBLE
    }

    public enum UserRole {
        USER,
        ADMIN
    }


}

