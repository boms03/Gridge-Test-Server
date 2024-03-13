package com.example.demo.src.mapping.boardReport.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.report.entity.Report;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "POST_REPORT")
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

}
