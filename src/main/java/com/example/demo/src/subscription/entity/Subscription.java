package com.example.demo.src.subscription.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.order.entity.Purchase;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name="order_id", nullable = false)
    private Purchase purchase;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscriptionState", nullable = false, length = 20)
    protected Constant.SubscriptionState subscriptionState;

    @Builder
    public Subscription(User user, Purchase purchase, Constant.SubscriptionState subscriptionState){
        this.user = user;
        this.purchase = purchase;
        this.subscriptionState = subscriptionState;
    }

}
