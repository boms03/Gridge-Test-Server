package com.example.demo.common.config;

import com.example.demo.src.mapping.userAgree.UserAgreeRepository;
import com.example.demo.src.mapping.userAgree.entity.UserAgree;
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
}