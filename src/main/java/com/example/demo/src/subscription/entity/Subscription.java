package com.example.demo.src.subscription.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.order.entity.Purchase;
import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name="purchase_id", nullable = false)
    private Purchase purchase;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_state", nullable = false, length = 20)
    protected Constant.SubscriptionState subscriptionState;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "end_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endAt;

    @Builder
    public Subscription(User user, Purchase purchase, Constant.SubscriptionState subscriptionState, LocalDateTime endAt){
        this.user = user;
        this.purchase = purchase;
        this.subscriptionState = subscriptionState;
        this.endAt = endAt;
    }

}
