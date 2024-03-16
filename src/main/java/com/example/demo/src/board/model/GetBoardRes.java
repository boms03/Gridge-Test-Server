package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetCommentsRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetBoardRes {

    private Long boardId;
    private String username;
    private String content;
    private String previewContent;
    private String location;
    private Long countLike;
    private String profileImageUrl;
    private List<String> imageUrlList;
    private String createdAt;
    private boolean isShortened;
    private Long countComment;
    private GetCommentsRes getCommentsRes;
    public GetBoardRes(Board board, String previewContent, boolean isShortened, Long countLike, List<String> imageUrlList, String formattedTime, GetCommentsRes getCommentsRes, Long countComment){
        this.boardId = board.getId();
        this.username = board.getUser().getUsername();
        this.content = board.getContent();
        this.previewContent = previewContent;
        this.isShortened = isShortened;
        this.location = board.getLocation();
        this.countLike = countLike;
        this.imageUrlList = imageUrlList;
        this.createdAt = formattedTime;
        this.getCommentsRes = getCommentsRes;
        this.countComment = countComment;
   }
}
