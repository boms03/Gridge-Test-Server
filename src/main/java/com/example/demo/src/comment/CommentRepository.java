package com.example.demo.src.comment;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByIdAndState(Long id, Constant.State state);

    Page<Comment> findByIdLessThanAndStateAndBoardOrderByIdDesc(Long id, Constant.State state, Board board,PageRequest pageRequest);

    Page<Comment>findTop10ByStateAndBoardOrderByIdDesc(Constant.State state, Board board, PageRequest pageRequest);

    List<Comment> findAllByBoardAndState(Board board, Constant.State state);

    Long countAllByBoardAndState(Board board, Constant.State state);
}
