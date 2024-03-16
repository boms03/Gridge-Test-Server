package com.example.demo.src.comment;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetCommentsRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @PostMapping("")
    public BaseResponse<String> createComment(
            @RequestBody PostCommentReq postCommentReq,
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

    @GetMapping("")
    public BaseResponse<GetCommentRes> getComment(
            @RequestParam Long commentId
    ){
        GetCommentRes getCommentRes = commentService.getComment(commentId);
        return new BaseResponse<>(getCommentRes);
    }

    @GetMapping("/all")
    public BaseResponse<GetCommentsRes> getComments(
            @RequestParam Long boardId,
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

    @DeleteMapping("")
    public BaseResponse<String> deleteComment(
            @RequestParam Long commentId,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        commentService.deleteComment(commentId,id);
        String response = "댓글 지우기 완료";
        return new BaseResponse<>(response);
    }

}
