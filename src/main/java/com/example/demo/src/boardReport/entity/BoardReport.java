package com.example.demo.src.boardReport.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "BOARD_REPORT")
public class BoardReport extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected Constant.State state = Constant.State.ACTIVE;

    @Builder
    public BoardReport(Report report, Board board, User user){
        this.report = report;
        this.board = board;
        this.user = user;
    }

}
