package com.example.demo.src.payment;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.payment.entity.Prepayment;
import com.example.demo.src.payment.model.BuyerRes;
import com.example.demo.src.payment.model.PayReq;
import com.example.demo.utils.JwtService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtService jwtService;

    @GetMapping("/payment/info")
    public BuyerRes getBuyerInfo(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        return paymentService.getBuyerInfo(userId);
    }

    @PostMapping("/payment/prepare")
    public PrepareData preparePayment() {
        return paymentService.preValidatePayment();
    }


    @PostMapping("/payment/validate")
    public Payment validatePayment(@RequestBody PayReq request) {
        return paymentService.validatePayment(request);
    }

    @PostMapping("/payment/cancel")
    public BaseResponse<String> cancelPayment(@RequestParam String imp_uid) {
        paymentService.cancelPayment(imp_uid);
        String response = imp_uid+" 결제 취소 완료";
        return new BaseResponse<>(response);
    }

}
