package com.example.demo.src.user.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.QUser;
import com.example.demo.src.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public Page<User> findUsersBySearchOption(Pageable pageable, String username, String name, String createdAt, Constant.UserState state) {

        JPQLQuery<User> query =  queryFactory.selectFrom(user)
                .where(eqUsername(username), eqName(name), eqCreatedAt(createdAt), eqUserState(state))
                .orderBy(user.createdAt.desc());

        long totalCount = query.fetchCount();
        List<User> users = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(users, pageable, totalCount);
    }

    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return user.username.eq(username);
    }

    private BooleanExpression eqName(String name) {
        if(name == null || name.isEmpty()) {
            return null;
        }
        return user.name.eq(name);
    }

    private BooleanExpression eqCreatedAt(String createdAt) {
        if(createdAt == null || createdAt.isEmpty()) {
            return null;
        }

        StringExpression formattedDate = Expressions.stringTemplate("FUNCTION('DATE_FORMAT', {0}, '%Y%m%d')", user.createdAt);

        return formattedDate.eq(createdAt);
    }

    private BooleanExpression eqUserState(Constant.UserState state) {
        if(state == null) {
            return null;
        }
        return user.state.eq(state);
    }
}
