package com.example.demo.src.comment.model;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentReq {

    private Long boardId;
    private String content;

}
