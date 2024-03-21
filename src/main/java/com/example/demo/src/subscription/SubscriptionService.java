package com.example.demo.src.subscription;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.subscription.entity.Subscription;
import com.example.demo.src.subscription.repository.SubscriptionRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.UnscheduleData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final IamportClient iamportClient;

    public void subscribe(Long userId){

        Subscription subscription = subscriptionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_SUBSCRIPTION));

        if(subscription.getSubscriptionState() == Constant.SubscriptionState.SUBSCRIBED){

            throw new BaseException(BaseResponseStatus.ALREADY_SUBSCRIBED);

        }

    }
    public void unsubscribe(Long userId){

        Subscription subscription = subscriptionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_SUBSCRIPTION));

        if(subscription.getSubscriptionState() == Constant.SubscriptionState.UNSUBSCRIBED){

            throw new BaseException(BaseResponseStatus.NOT_SUBSCRIBED);

        }

        subscriptionRepository.save(new Subscription(subscription.getUser(),subscription.getPurchase(), Constant.SubscriptionState.UNSUBSCRIBED, subscription.getEndAt()));

        UnscheduleData unscheduleData = new UnscheduleData(subscription.getPurchase().getCustomerUid());

        try{

            iamportClient.unsubscribeSchedule(unscheduleData);

        } catch (IamportResponseException | IOException e){

            throw new BaseException(BaseResponseStatus.IAMPORT_ERROR);

        }

        subscriptionRepository.save(new Subscription(subscription.getUser(),subscription.getPurchase(), Constant.SubscriptionState.UNSUBSCRIBED, subscription.getEndAt()));

    }
}
