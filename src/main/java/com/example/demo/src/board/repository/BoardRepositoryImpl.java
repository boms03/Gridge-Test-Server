package com.example.demo.src.board.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.board.entity.QBoard;
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

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;

    public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }
    public Page<Board> findBoardsBySearchOption(Pageable pageable, String username, String createdAt, Constant.BoardState state) {

        JPQLQuery<Board> query;

        if((username == null) || (username.isEmpty()) && (createdAt == null) || (createdAt.isEmpty()) && (state == null)){
            query = queryFactory.selectFrom(board)
                    .orderBy(board.createdAt.desc());
        } else {
            query =  queryFactory.selectFrom(board)
                    .where(eqUsername(username), eqCreatedAt(createdAt), eqBoardState(state))
                    .orderBy(board.user.createdAt.desc());
        }

        long totalCount = query.fetchCount();
        List<Board> boards = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(boards, pageable, totalCount);
    }

    private BooleanExpression eqUsername(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return board.user.username.eq(username);
    }

    private BooleanExpression eqCreatedAt(String createdAt) {
        if(createdAt == null || createdAt.isEmpty()) {
            return null;
        }

        StringExpression formattedDate = Expressions.stringTemplate("FUNCTION('DATE_FORMAT', {0}, '%Y%m%d')", board.createdAt);

        return formattedDate.eq(createdAt);
    }

    private BooleanExpression eqBoardState(Constant.BoardState state) {
        if(state == null) {
            return null;
        }
        return board.boardState.eq(state);
    }
}
