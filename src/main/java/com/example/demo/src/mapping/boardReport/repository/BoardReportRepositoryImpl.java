package com.example.demo.src.mapping.boardReport.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.mapping.boardReport.entity.BoardReport;
import com.example.demo.src.mapping.boardReport.entity.QBoardReport;
import com.example.demo.src.subscription.SubscriptionService;
import com.example.demo.src.subscription.entity.QSubscription;
import com.example.demo.src.subscription.entity.Subscription;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardReportRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final QBoardReport boardReport = QBoardReport.boardReport;

    public BoardReportRepositoryImpl(JPAQueryFactory queryFactory) {
        super(BoardReport.class);
        this.queryFactory = queryFactory;
    }

    public Page<BoardReport> findBoardReportsBySearchOption(Pageable pageable) {
        JPQLQuery<BoardReport> query =  queryFactory.selectFrom(boardReport)
                .where(eqBoardReportState())
                .orderBy(boardReport.createdAt.desc());

        long totalCount = query.fetchCount();
        List<BoardReport> boardReports = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(boardReports, pageable, totalCount);
    }

    private BooleanExpression eqBoardReportState() {
        return boardReport.state.eq(Constant.State.ACTIVE);
    }
}
