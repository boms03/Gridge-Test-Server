package com.example.demo.src.comment;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetCommentsRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @Operation(summary = "Post 댓글 생성")
    @ApiResponse(responseCode = "200", description = "댓글 생성 성공")
    @ApiResponse(responseCode = "400", description = "댓글 생성 실패")
    @PostMapping("")
    public BaseResponse<String> createComment(
            @RequestBody @Valid PostCommentReq postCommentReq,
            HttpServletRequest request
    ){
        if(postCommentReq.getContent().length() > 2200 || postCommentReq.getContent().isEmpty()){
            throw new BaseException(BaseResponseStatus.INVALID_LENGTH_COMMENT);
        }
        Long id = jwtService.getUserId(request);
        commentService.createComment(postCommentReq,id);
        String response = "댓글 업로드 완료";
        return new BaseResponse<>(response);
    }


    @Operation(summary = "Get 댓글 조회")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    @ApiResponse(responseCode = "400", description = "댓글 조회 실패")
    @GetMapping("")
    public BaseResponse<GetCommentRes> getComment(
            @Parameter(required = true, description = "댓글 아이디")
            @RequestParam Long commentId
    ){
        GetCommentRes getCommentRes = commentService.getComment(commentId);
        return new BaseResponse<>(getCommentRes);
    }

    @Operation(summary = "Get 해당 게시글에 달린 모든 댓글 조회")
    @ApiResponse(responseCode = "200", description = "해당 게시글에 달린 모든 댓글 조회 성공")
    @ApiResponse(responseCode = "400", description = "해당 게시글에 달린 모든 댓글 조회 실패")
    @GetMapping("/all")
    public BaseResponse<GetCommentsRes> getComments(
            @Parameter(required = true, description = "게시글 아이디")
            @RequestParam Long boardId,
            @Parameter(required = false, description = "마지막으로 불러온 댓글 아이디. 게시글 첫 로딩시 null.")
            @RequestParam(required = false) Long lastCommentId

    ){
        GetCommentsRes getCommentsRes;

        if(lastCommentId == null) {
            getCommentsRes= commentService.fetchCommentPagesBy(boardId,-1L);
        } else {
            getCommentsRes= commentService.fetchCommentPagesBy(boardId,lastCommentId);
        }

        return new BaseResponse<>(getCommentsRes);
    }

    @Operation(summary = "Put 댓글 지우기")
    @ApiResponse(responseCode = "200", description = "댓글 지우기 성공")
    @ApiResponse(responseCode = "400", description = "댓글 지우기 실패")
    @PutMapping("")
    public BaseResponse<String> deleteComment(
            @Parameter(required = true, description = "댓글 아이디")
            @RequestParam Long commentId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        commentService.deleteComment(commentId,id);
        String response = "댓글 지우기 완료";
        return new BaseResponse<>(response);
    }

}
