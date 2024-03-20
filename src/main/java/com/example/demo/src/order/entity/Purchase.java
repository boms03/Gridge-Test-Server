package com.example.demo.src.order.entity;

import com.example.demo.common.Constant;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "PURCHASE")
public class Purchase {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String merchantUid; // 주문 번호

    private String customerUid; // 빌링키

    private String payMethod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 10)
    protected Constant.PurchaseState state;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Purchase(BigDecimal amount, String merchantUid, String customerUid, String payMethod, User user, Constant.PurchaseState orderState) {
        this.amount = amount;
        this.merchantUid = merchantUid;
        this.customerUid = customerUid;
        this.payMethod = payMethod;
        this.user = user;
        this.state = orderState;
    }

    @Builder
    public Purchase(String merchantUid, Constant.PurchaseState orderState) {
        this.merchantUid = merchantUid;
        this.state = orderState;
    }
}
