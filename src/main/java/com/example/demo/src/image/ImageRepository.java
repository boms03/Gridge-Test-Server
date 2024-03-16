package com.example.demo.src.image;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findAllByBoardOrderByCreatedAtAsc(Board board);
}
