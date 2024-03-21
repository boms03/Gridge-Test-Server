package com.example.demo.src.board.model;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostBoardReq {
    @NotNull
    private String content;
    @NotNull
    private String location;

    public Board toEntity(User user) {
        return Board.builder()
                .content(this.content)
                .location(this.location)
                .user(user)
                .build();
    }
}
