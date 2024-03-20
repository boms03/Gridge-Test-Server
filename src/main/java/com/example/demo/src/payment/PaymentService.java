package com.example.demo.src.payment;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.payment.entity.Prepayment;
import com.example.demo.src.payment.model.BuyerRes;
import com.example.demo.src.payment.model.PayReq;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.request.ScheduleData;
import com.siot.IamportRestClient.request.ScheduleEntry;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Prepare;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final IamportClient iamportClient;
    private final PrepaymentRepository prepaymentRepository;
    private final UserRepository userRepository;
    private final BigDecimal cost = BigDecimal.valueOf(30);

    public BuyerRes getBuyerInfo(Long userId) {
        User user = userRepository.findByIdAndState(userId, Constant.UserState.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        return BuyerRes.builder()
                .name(user.getName())
                .phone(user.getPhoneNumber())
                .build();
    }

    public PrepareData preValidatePayment() {

        String merchantUid = "O" + new Date().getTime();
        ;

        PrepareData prepareData = new PrepareData(merchantUid, cost);

        try {

            iamportClient.postPrepare(prepareData);

        } catch (IamportResponseException | IOException e) {

            throw new BaseException(BaseResponseStatus.SERVER_ERROR);

        }

        prepaymentRepository.save(new Prepayment(cost, merchantUid));
        return prepareData;
    }

    public Payment validatePayment(PayReq request) {

        Prepayment prePayment = prepaymentRepository.findByMerchantUid(request.getMerchant_uid())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.IAMPORT_ERROR));

        BigDecimal preAmount = prePayment.getAmount();

        IamportResponse<Payment> iamportResponse;

        try {

            iamportResponse = iamportClient.paymentByImpUid(request.getImp_uid());

            BigDecimal paidAmount = iamportResponse.getResponse().getAmount();

            if (!Objects.equals(preAmount, paidAmount)) {
                cancelPayment(request.getImp_uid());
            }

            ScheduleData scheduleData = new ScheduleData(request.getCustomer_uid());

            LocalDateTime currentTime = LocalDateTime.now();

            for(int i=1;i<=12;i++){
                LocalDateTime monthsAfterLocalDate = currentTime.plusMonths(i);
                Date monthsAfterDate = java.sql.Timestamp.valueOf(monthsAfterLocalDate);
                ScheduleEntry scheduleEntry = new ScheduleEntry("O" + new Date().getTime(), monthsAfterDate, cost);
                scheduleData.addSchedule(scheduleEntry);
            }

            iamportClient.subscribeSchedule(scheduleData);

        } catch (IamportResponseException | IOException e) {
            throw new BaseException(BaseResponseStatus.IAMPORT_ERROR);
        }

        return iamportResponse.getResponse();
    }


    public void cancelPayment(String imp_uid) {

        try {

            CancelData cancelData = new CancelData(imp_uid, true);
            iamportClient.cancelPaymentByImpUid(cancelData);

        } catch (IamportResponseException | IOException e) {

            throw new BaseException(BaseResponseStatus.SERVER_ERROR);

        }
    }
}
