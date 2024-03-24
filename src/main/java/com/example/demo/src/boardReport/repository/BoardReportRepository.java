package com.example.demo.src.boardReport.repository;

import com.example.demo.src.boardReport.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardReportRepository extends JpaRepository<BoardReport,Long>, BoardReportRepositoryCustom {
    Optional<BoardReport> findByBoardIdAndUserId(Long boardId,Long userId);
}
