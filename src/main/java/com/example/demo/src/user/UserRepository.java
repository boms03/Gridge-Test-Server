package com.example.demo.src.user;

import com.example.demo.common.Constant.*;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndState(Long id, UserState state);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndState(String username, UserState state);
    Optional<User> findByEmailAndState(String email, UserState state);
    List<User> findAllByEmailAndState(String email, UserState state);
    List<User> findAllByState(UserState state);

}
