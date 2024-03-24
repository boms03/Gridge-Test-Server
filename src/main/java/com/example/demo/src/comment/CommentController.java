package com.example.demo.src.comment;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetCommentsRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    /**
     * 댓글 생성 API
     * [POST] /comments
     * @param postCommentReq 댓글 등록에 필요한 정보 dto
     * @return BaseResponse<String>
     */
    @Operation(summary = "Post 댓글 생성")
    @ApiResponse(responseCode = "200", description = "댓글 생성 성공")
    @ApiResponse(responseCode = "400", description = "댓글 생성 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
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

    /**
     * 댓글 한개 조회 API
     * [GET] /comments/{commentId}
     * @param commentId 조회할 댓글 아이디
     * @return BaseResponse<GetCommentRes>
     */
    @Operation(summary = "Get 댓글 조회")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/{commentId}")
    public BaseResponse<GetCommentRes> getComment(
            @Parameter(required = true, description = "댓글 아이디")
            @PathVariable Long commentId
    ){
        GetCommentRes getCommentRes = commentService.getComment(commentId);
        return new BaseResponse<>(getCommentRes);
    }


    /**
     * 특정 게시글에 달린 댓글 무한 스크롤 방식으로 조회 API
     * [GET] /comments
     * @param boardId 댓글 조회할 게시글 아이디
     * @param lastCommentId 마지막으로 조회한 댓글 아이디. 첫 로딩시 null.
     * @return BaseResponse<GetCommentRes>
     */
    @Operation(summary = "Get 해당 게시글에 달린 모든 댓글 조회")
    @ApiResponse(responseCode = "200", description = "해당 게시글에 달린 모든 댓글 조회 성공")
    @ApiResponse(responseCode = "400", description = "해당 게시글에 달린 모든 댓글 조회 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("")
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

    /**
     * 댓글 삭제 API
     * [PUT] /comments
     * @param commentId 삭제할 댓글 아이디
     * @return BaseResponse<GetCommentRes>
     */
    @Operation(summary = "Put 댓글 지우기")
    @ApiResponse(responseCode = "200", description = "댓글 지우기 성공")
    @ApiResponse(responseCode = "400", description = "댓글 지우기 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
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
