package com.example.demo.src.terms;

import com.example.demo.src.terms.entity.Terms;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}
