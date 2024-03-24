package com.example.demo.src.board;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.board.model.*;
import com.example.demo.src.boardReport.BoardReportService;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardReportService boardReportService;
    private final JwtService jwtService;

    @Operation(summary = "Post 게시글 생성")
    @ApiResponse(responseCode = "200", description = "게시글 생성 성공")
    @ApiResponse(responseCode = "400", description = "게시글 생성 실패")
    @PostMapping("")
    public BaseResponse<String> createBoard(
            @Parameter(required = true, name = "이미지 및 영상 파일")
            @RequestPart(value="files") List<MultipartFile> files,

            @RequestPart(value = "requestDto") @Valid PostBoardReq postBoardReq,
            HttpServletRequest request
    ){
        if(files.size()>10){
            throw new BaseException(BaseResponseStatus.LIMIT_FILE);
        }
        if(postBoardReq.getContent().length() > 2200 || postBoardReq.getContent().isEmpty()){
            throw new BaseException(BaseResponseStatus.INVALID_LENGTH_BOARD);
        }

        Long id = jwtService.getUserId(request);
        boardService.createBoard(files,postBoardReq,id);
        String response = "게시물 업로드 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Get 게시글 조회")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
    @ApiResponse(responseCode = "400", description = "게시글 조회 실패")
    @GetMapping("")
    public BaseResponse<GetBoardRes> getBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        GetBoardRes getBoardRes = boardService.getBoard(boardId, id);

        return new BaseResponse<>(getBoardRes);
    }

    @Operation(summary = "Put 게시글 삭제")
    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시글 삭제 실패")
    @PutMapping("")
    public BaseResponse<String> deleteBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.deletePost(boardId,id);
        String response = "게시물 삭제 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Get 모든 게시글 조회", description = "무한 스크롤 방식이며 마지막으로 조회한 게시글 아이디를 저장하며 조회")
    @ApiResponse(responseCode = "200", description = "모든 게시글 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 게시글 조회 실패")
    @GetMapping("/all")
    public BaseResponse<GetBoardsRes> getBoards(
            @Parameter(required = false, name = "마지막으로 조회한 게시글 아이디")
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

    @Operation(summary = "Post 게시글 좋아요")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공")
    @ApiResponse(responseCode = "400", description = "게시글 좋아요 실패")
    @PostMapping("/like")
    public BaseResponse<String> likeBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.likeBoard(id, boardId);
        String response = "게시물 좋아요 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Delete 게시글 좋아요 삭제")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시글 좋아요 삭제 실패")
    @DeleteMapping("/like")
    public BaseResponse<String> unlikeBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardService.unlikeBoard(id, boardId);
        String response = "게시물 좋아요 취소 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Post 게시글 신고")
    @ApiResponse(responseCode = "200", description = "게시글 신고")
    @ApiResponse(responseCode = "400", description = "게시글 신고")
    @PostMapping("/report")
    public BaseResponse<String> reportBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId,

            @Parameter(required = true, name = "신고 카테고리 아이디", description = "(아이디) 1. 스팸, 2. 나체 이미지 또는 성적 행위, 3. 혐오 발언 또는 상징, 4. 폭력 또는 위험한 단체, 5. 불법 또는 규제 상품 판매, 6. 따돌림 또는 괴롭힘, 7. 지식재산권 침해, 8. 자살 또는 자해, 9. 섭식 장애, 10. 사기 또는 거짓, 11. 거짓 정보, 12. 마음에 들지 않습니다")
            @RequestParam Long reportId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        boardReportService.reportBoard(id, boardId, reportId);
        String response = "게시물 신고 완료";
        return new BaseResponse<>(response);
    }
}
