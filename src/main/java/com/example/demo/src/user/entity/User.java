package com.example.demo.src.user.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.mapping.boardLike.entity.BoardLike;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.mapping.boardReport.entity.BoardReport;
import com.example.demo.src.mapping.follow.entity.Follow;
import com.example.demo.src.mapping.userAgree.entity.UserAgree;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.order.entity.Purchase;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String email;

    @Column(length = 100)
    private String phoneNumber;

    @Column(length = 20)
    private String username;

    @Column(length = 300)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    private Constant.UserRole role;

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private boolean isOAuth;

    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    protected Constant.UserState state = Constant.UserState.ACTIVE;

    // Oauth 생일 정보 필수가 아니라서 nullable
    private Date birth;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastAgreedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAgree> userAgreeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BoardReport> boardReportList = new ArrayList<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private List<Follow> followeeList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Purchase> purchaseList = new ArrayList<>();


    @Builder
    public User(Long id, String email, String phoneNumber, String username, String password, String name, boolean isOAuth, Date birth, LocalDateTime lastAgreedAt, String provider, Constant.UserRole role) {
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.name = name;
        this.isOAuth = isOAuth;
        this.birth = birth;
        this.lastAgreedAt = lastAgreedAt;
        this.provider = provider;
        this.role = role;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = Constant.UserState.WITHDRAW;
    }

}
