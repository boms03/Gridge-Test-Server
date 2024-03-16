package com.example.demo.src.comment.model;

import com.example.demo.src.board.model.GetBoardRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsRes {
    private List<GetCommentRes> commentList;
    private Long lastCommentId;

    public GetCommentsRes(List<GetCommentRes> commentList){
        this.commentList = commentList;
    }

}


