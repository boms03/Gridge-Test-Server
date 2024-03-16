package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardsRes {
    private List<GetBoardRes> boardsList;
    private Long lastBoardId;

    public GetBoardsRes(List<GetBoardRes> boardsList){
        this.boardsList = boardsList;
    }

}
