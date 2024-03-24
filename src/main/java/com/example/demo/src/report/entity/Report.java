package com.example.demo.src.report.entity;

import com.example.demo.src.boardReport.entity.BoardReport;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "REPORT")
public class Report {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String title;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardReport> boardReportList = new ArrayList<>();
}
