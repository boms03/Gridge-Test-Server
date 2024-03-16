package com.example.demo.src.board;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.src.mapping.boardReport.BoardReportService;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardReportService boardReportService;
    private final JwtService jwtService;

    @PostMapping("")
    public BaseResponse<String> createBoard(
            @RequestPart(value="images") List<MultipartFile> files,
            @RequestPart(value = "requestDto") PostBoardReq postBoardReq,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.createBoard(files,postBoardReq,id);
        String response = "게시물 업로드 완료";
        return new BaseResponse<>(response);
    }

    @GetMapping("")
    public BaseResponse<GetBoardRes> getBoard(
            @RequestParam Long boardId
    ){
        GetBoardRes getBoardRes = boardService.getBoard(boardId);

        return new BaseResponse<>(getBoardRes);
    }

    @DeleteMapping("")
    public BaseResponse<String> deleteBoard(
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.deletePost(boardId,id);
        String response = "게시물 삭제 완료";
        return new BaseResponse<>(response);
    }

    @GetMapping("/all")
    public BaseResponse<GetBoardsRes> getBoards(
            @RequestParam(required = false) Long lastBoardId,
            HttpServletRequest request

    ){
        Long id = jwtService.getUserId(request);
        GetBoardsRes getBoardsRes;

        if(lastBoardId == null) {
            getBoardsRes= boardService.fetchBoardPagesBy(-1L, id);
        } else {
            getBoardsRes= boardService.fetchBoardPagesBy(lastBoardId, id);
        }
        return new BaseResponse<>(getBoardsRes);
    }

    @PostMapping("/like")
    public BaseResponse<String> likeBoard(
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.likeBoard(id, boardId);
        String response = "게시물 좋아요 완료";
        return new BaseResponse<>(response);
    }
    @DeleteMapping("/like")
    public BaseResponse<String> unlikeBoard(
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.unlikeBoard(id, boardId);
        String response = "게시물 좋아요 취소 완료";
        return new BaseResponse<>(response);
    }

    @PostMapping("/report")
    public BaseResponse<String> reportBoard(
            @RequestParam Long boardId,
            @RequestParam Long reportId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardReportService.reportBoard(id, boardId, reportId);
        String response = "게시물 신고 완료";
        return new BaseResponse<>(response);
    }
}
