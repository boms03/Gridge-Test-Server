package com.example.demo.src.boardLike;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.boardLike.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    Long countAllByBoard(Board board);
    Optional<BoardLike> findByBoardIdAndUserId(Long boardId, Long userId);
}
