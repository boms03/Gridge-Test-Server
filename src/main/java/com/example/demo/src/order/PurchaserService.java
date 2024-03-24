package com.example.demo.src.order;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.order.entity.Purchase;
import com.example.demo.src.order.model.PurchaseReq;
import com.example.demo.src.subscription.repository.SubscriptionRepository;
import com.example.demo.src.subscription.entity.Subscription;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaserService {

    private final PurchaseRepository orderRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public void saveOrder(PurchaseReq request) {

        User user = userRepository.findByPhoneNumber(request.getPhone())
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        if(request.isSuccess()){

            Purchase purchase = orderRepository.save(new Purchase(request.getAmount(), request.getMerchantUid(), request.getCustomerUid(), request.getPayMethod(), user, Constant.PurchaseState.SUCCESS));

            subscriptionRepository.save(new Subscription(user, purchase, Constant.SubscriptionState.SUBSCRIBED, LocalDateTime.now().plusMonths(1)));
        } else {

            orderRepository.save(new Purchase(request.getMerchantUid(), Constant.PurchaseState.FAILED));

        }

    }

}