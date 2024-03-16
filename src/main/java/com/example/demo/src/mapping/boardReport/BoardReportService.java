package com.example.demo.src.mapping.boardReport;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.board.BoardRepository;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.mapping.boardReport.entity.BoardReport;
import com.example.demo.src.report.ReportRepository;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardReportRepository boardReportRepository;
    public void reportBoard(Long userId, Long boardId, Long reportId){

        if(isBoardAuthor(userId,boardId)){
            throw new BaseException(BaseResponseStatus.OWN_REPORT);
        }

        if(isDuplicateReport(userId,boardId)){
            throw new BaseException(BaseResponseStatus.DUPLICATE_REPORT);
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_REPORT));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        boardReportRepository.save( new BoardReport(report,board,user));
    }

    public boolean isBoardAuthor(Long userId, Long boardId){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        return Objects.equals(board.getUser().getId(), userId);

    }

    public boolean isDuplicateReport(Long userId, Long boardId){

        return boardReportRepository.findByBoardIdAndUserId(boardId,userId).isPresent();

    }

}
