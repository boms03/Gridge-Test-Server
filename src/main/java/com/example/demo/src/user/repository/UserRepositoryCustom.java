package com.example.demo.src.user.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

public interface UserRepositoryCustom  {
    Page<User> findUsersBySearchOption(Pageable pageable, String username, String name, String createdAt, Constant.UserState state);
}
