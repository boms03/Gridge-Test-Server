package com.example.demo.src.comment.model;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentRes {
    private Long commentId;
    private String username;
    private String content;
    private String createdAt;

    public GetCommentRes(Comment comment, String formattedTime){
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = formattedTime;
    }
}
