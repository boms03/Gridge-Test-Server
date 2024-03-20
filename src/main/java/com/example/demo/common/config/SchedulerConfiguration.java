package com.example.demo.common.config;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.mapping.userAgree.UserAgreeRepository;
import com.example.demo.src.mapping.userAgree.entity.UserAgree;
import com.example.demo.src.subscription.SubscriptionRepository;
import com.example.demo.src.subscription.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.common.Constant;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    private final UserAgreeRepository userAgreeRepository;
    private final SubscriptionRepository subscriptionRepository;

    // 매일 00시 정각
    @Scheduled(cron = "0 0 0 * * *")
    public void checkUserAgree() {
        List<UserAgree> userAgreeList = userAgreeRepository.findAll();

        userAgreeList.stream()
                        .filter(userAgree-> LocalDateTime.now().minusYears(1).isBefore(userAgree.getUpdatedAt()))
                        .map(UserAgree::getUser)
                        .forEach(user -> user.setState(Constant.UserState.RENEW));

        log.info("약관 동의 재갱신 필요한 회원 업데이트");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkUserSubscription() {
        List<Subscription> subscriptionList = subscriptionRepository.findAllBySubscriptionState(Constant.SubscriptionState.SUBSCRIBED);

        subscriptionList.stream()
                .filter(subscription -> LocalDateTime.now().minusMonths(1).isBefore(subscription.getCreatedAt()))
                .map(Subscription::getUser)
                .forEach(user -> {

                    Subscription subscription = subscriptionRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId())
                                    .orElseThrow( () -> new BaseException(BaseResponseStatus.NOT_FIND_SUBSCRIPTION));

                    subscriptionRepository.save(new Subscription(user,subscription.getPurchase(), Constant.SubscriptionState.UNSUBSCRIBED));
                });

        log.info("구독 만료 업데이트");
    }
}