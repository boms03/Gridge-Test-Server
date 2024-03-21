package com.example.demo.src.subscription.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.subscription.entity.Subscription;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long>, SubscriptionRepositoryCustom{
    Optional<Subscription>findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    List<Subscription> findAllBySubscriptionState(Constant.SubscriptionState subscriptionState);

}
