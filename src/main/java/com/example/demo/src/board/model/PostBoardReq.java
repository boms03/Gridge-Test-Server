package com.example.demo.src.board.model;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostBoardReq {
    private String content;
    private String location;
    private Constant.State state;

    public Board toEntity(User user) {
        return Board.builder()
                .content(this.content)
                .location(this.location)
                .state(Constant.State.ACTIVE)
                .user(user)
                .build();
    }
}
