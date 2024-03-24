package com.example.demo.src.board.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.image.entity.Image;
import com.example.demo.src.boardLike.entity.BoardLike;
import com.example.demo.src.boardReport.entity.BoardReport;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "BOARD")
public class Board extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "VARCHAR(100)")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected Constant.State state = Constant.State.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_state", nullable = false, length = 10)
    protected Constant.BoardState boardState = Constant.BoardState.VISIBLE;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLike> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardReport> boardReportList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Board(String content, String location, User user){
        this.content = content;
        this.location = location;
        this.user = user;
    }

    public void deleteBoard() {
        this.state = Constant.State.INACTIVE;
    }

}
