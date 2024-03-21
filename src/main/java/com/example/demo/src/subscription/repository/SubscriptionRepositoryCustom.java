package com.example.demo.src.subscription.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.subscription.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionRepositoryCustom {
    public Page<Subscription> findSubscriptionsBySearchOption(Pageable pageable, String username, String name, String createdAt, String endAt, Constant.SubscriptionState state);
}
