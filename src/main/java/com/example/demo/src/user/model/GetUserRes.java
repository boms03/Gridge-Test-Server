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
public class GetUserRes {
    private Long id;
    private String email;
    private String name;
    private Constant.UserRole role;

    public GetUserRes(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
    }
}
