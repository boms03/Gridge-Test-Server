package com.example.demo.src.payment;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.payment.entity.Prepayment;
import com.example.demo.src.payment.model.BuyerRes;
import com.example.demo.src.payment.model.PayReq;
import com.example.demo.utils.JwtService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get 결제자 정보")
    @ApiResponse(responseCode = "200", description = "결제자 정보 가져오기 성공")
    @ApiResponse(responseCode = "400", description = "결제자 정보 가져오기 실패")
    @GetMapping("/payment/info")
    public BuyerRes getBuyerInfo(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        return paymentService.getBuyerInfo(userId);
    }

    @Operation(summary = "Post 결제 금액 사전 검증 등록", description = "스크립트 조작으로 인한 금액 변조를 방지하기 위해 구독 페이지 로딩 즉시 API 호출")
    @ApiResponse(responseCode = "200", description = "결제 금액 사전 검증 등록 성공")
    @ApiResponse(responseCode = "400", description = "결제 금액 사전 검증 등록 실패")
    @PostMapping("/payment/prepare")
    public PrepareData preparePayment() {
        return paymentService.preValidatePayment();
    }


    @Operation(summary = "Post 결제 금액 사후 검증", description = "실제 금액과 결제된 금액 동일 여부 체크")
    @ApiResponse(responseCode = "200", description = "결제 금액 사전 검증 등록 성공")
    @ApiResponse(responseCode = "400", description = "결제 금액 사전 검증 등록 실패")
    @PostMapping("/payment/validate")
    public Payment validatePayment(@RequestBody PayReq request) {
        return paymentService.validatePayment(request);
    }

    @Operation(summary = "Post 결제 취소")
    @ApiResponse(responseCode = "200", description = "결제 취소 성공")
    @ApiResponse(responseCode = "400", description = "결제 취소 실패")
    @PostMapping("/payment/cancel")
    public BaseResponse<String> cancelPayment(@RequestParam String imp_uid) {
        paymentService.cancelPayment(imp_uid);
        String response = imp_uid+" 결제 취소 완료";
        return new BaseResponse<>(response);
    }

}
