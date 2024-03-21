package com.example.demo.src.user.repository;

import com.example.demo.common.Constant.*;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByIdAndState(Long id, UserState state);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndState(String username, UserState state);
    Optional<User> findByEmailAndState(String email, UserState state);
    List<User> findAllByEmailAndState(String email, UserState state);
    List<User> findAllByState(UserState state);

}
