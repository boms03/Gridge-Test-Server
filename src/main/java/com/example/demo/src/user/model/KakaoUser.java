package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    public Long id;
    public String nickname;
    public String email;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password("NONE")
                .name(this.nickname)
                .isOAuth(true)
                .provider(Constant.SocialLoginType.KAKAO.toString())
                .build();
    }
}
