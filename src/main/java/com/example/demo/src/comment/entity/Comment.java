package com.example.demo.src.comment.entity;

import com.example.demo.common.Constant;
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
@Table(name = "COMMENT")
public class Comment extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected Constant.State state = Constant.State.ACTIVE;

    @Builder
    public Comment(Board board,User user, String content){
        this.board = board;
        this.user = user;
        this.content = content;
    }

    public void deleteComment(){
        this.state = Constant.State.INACTIVE;
    }
}
