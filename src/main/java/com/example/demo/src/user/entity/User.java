package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.mapping.userAgree.UserAgree;
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
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String email;

    @Column(nullable = false, length = 100)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private boolean isOAuth;

    private String provider;

    // Oauth 생일 정보 필수가 아니라서 nullable
    private Date birth;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime lastAgreedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAgree> userAgreeList = new ArrayList<>();


    @Builder
    public User(Long id, String email, String phoneNumber, String username, String password, String name, boolean isOAuth, Date birth, LocalDateTime lastAgreedAt, String provider) {
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
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }

}
