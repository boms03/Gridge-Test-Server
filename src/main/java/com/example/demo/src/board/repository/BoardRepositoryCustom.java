package com.example.demo.src.board.repository;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findBoardsBySearchOption(Pageable pageable, String username, String name, Constant.BoardState state);
}
