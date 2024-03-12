package com.example.demo.src.mapping;

import com.example.demo.src.mapping.userAgree.UserAgree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgreeRepository extends JpaRepository<UserAgree,Long> {
}
