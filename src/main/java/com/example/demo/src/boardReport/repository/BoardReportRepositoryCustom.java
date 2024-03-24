package com.example.demo.src.boardReport.repository;

import com.example.demo.src.boardReport.entity.BoardReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReportRepositoryCustom {
    public Page<BoardReport> findBoardReportsBySearchOption(Pageable pageable);
}
