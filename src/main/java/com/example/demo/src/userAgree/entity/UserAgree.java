package com.example.demo.src.userAgree.entity;


import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.terms.entity.Terms;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "USER_AGREE")
public class UserAgree extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    @Builder
    public UserAgree(User user, Terms terms){
        this.user = user;
        this.terms = terms;
    }
}
