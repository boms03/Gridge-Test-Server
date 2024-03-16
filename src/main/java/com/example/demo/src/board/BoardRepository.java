package com.example.demo.src.board;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board>findByIdLessThanAndStateAndUserInOrderByIdDesc(Long id, Constant.State state, List<User> user, PageRequest pageRequest);

    Page<Board>findTop10ByStateAndUserInOrderByIdDesc(Constant.State state, List<User> user, PageRequest pageRequest);

    Optional<Board>findByIdAndState(Long id, Constant.State state);
}
