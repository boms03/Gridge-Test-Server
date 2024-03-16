package com.example.demo.src.mapping.boardLike.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "BOARD_LIKE")
public class BoardLike extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Builder
    public BoardLike(Board board, User user){
        this.board = board;
        this.user = user;
    }
}
