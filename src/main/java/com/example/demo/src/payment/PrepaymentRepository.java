package com.example.demo.src.payment;

import com.example.demo.src.payment.entity.Prepayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrepaymentRepository extends JpaRepository<Prepayment,Long> {
    Optional<Prepayment>findByMerchantUid(String merchantUid);
}
