package com.example.demo.src.subscription.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.subscription.entity.QSubscription;
import com.example.demo.src.subscription.entity.Subscription;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class SubscriptionRepositoryImpl extends QuerydslRepositorySupport implements SubscriptionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QSubscription subscription = QSubscription.subscription;

    public SubscriptionRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Subscription.class);
        this.queryFactory = queryFactory;
    }

    public Page<Subscription> findSubscriptionsBySearchOption(Pageable pageable, String username, String name, String createdAt, String endAt, Constant.SubscriptionState state) {
        JPQLQuery<Subscription> query =  queryFactory.selectFrom(subscription)
                .where(eqUsername(username), eqName(name), rCreatedAt(createdAt), rEndAt(endAt), eqSubscriptionState(state))
                .orderBy(subscription.createdAt.desc());

        long totalCount = query.fetchCount();
        List<Subscription> users = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(users, pageable, totalCount);
    }


    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return subscription.user.username.eq(username);
    }

    private BooleanExpression eqName(String name) {
        if(name == null || name.isEmpty()) {
            return null;
        }
        return subscription.user.name.eq(name);
    }

    private BooleanExpression rCreatedAt(String createdAt) {
        if(createdAt == null || createdAt.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inputDateTime = LocalDateTime.parse(createdAt, formatter);

        return subscription.createdAt.after(inputDateTime).or(subscription.createdAt.eq(inputDateTime));
    }

    private BooleanExpression rEndAt(String endAt) {
        if(endAt == null || endAt.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inputDateTime = LocalDateTime.parse(endAt, formatter);

        return subscription.endAt.before(inputDateTime).or(subscription.endAt.eq(inputDateTime));
    }

    private BooleanExpression eqSubscriptionState(Constant.SubscriptionState state) {
        if(state == null) {
            return null;
        }
        return subscription.subscriptionState.eq(state);
    }


}
