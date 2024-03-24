package com.example.demo.src.payment;

import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.payment.model.BuyerRes;
import com.example.demo.src.payment.model.PayReq;
import com.example.demo.utils.JwtService;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtService jwtService;

    /**
     * 구독 버튼 클릭시 결제자 정보 조회 API
     * /resources/template/payment.html 참고!
     * [GET] /payments/info
     * @return BaseResponse<BuyerRes>
     */
    @Operation(summary = "Get 결제 요청 전 결제자 정보 조회")
    @ApiResponse(responseCode = "200", description = "결제자 정보 가져오기 성공")
    @ApiResponse(responseCode = "400", description = "결제자 정보 가져오기 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/info")
    public BaseResponse<BuyerRes> getBuyerInfo(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        return new BaseResponse<>(paymentService.getBuyerInfo(userId));
    }

    /**
     * 결제 금액 사전 검증 등록 API
     * /resources/template/payment.html 참고!
     * [POST] /payments/prepare
     * @return BaseResponse<PrepareData>
     */
    @Operation(summary = "Post 결제 금액 사전 검증 등록", description = "스크립트 조작으로 인한 금액 변조를 방지하기 위해 구독 페이지 로딩 즉시 API 호출")
    @ApiResponse(responseCode = "200", description = "결제 금액 사전 검증 등록 성공")
    @ApiResponse(responseCode = "400", description = "결제 금액 사전 검증 등록 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("/prepare")
    public BaseResponse<PrepareData> preparePayment() {
        return new BaseResponse<>(paymentService.preValidatePayment());
    }

    /**
     * 결제 금액 사후 검증 API
     * /resources/template/payment.html 참고!
     * [POST] /payments/validate
     * @return BaseResponse<Payment>
     */
    @Operation(summary = "Post 결제 금액 사후 검증", description = "실제 금액과 결제된 금액 동일 여부 체크")
    @ApiResponse(responseCode = "200", description = "결제 금액 사전 검증 등록 성공")
    @ApiResponse(responseCode = "400", description = "결제 금액 사전 검증 등록 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("/validate")
    public BaseResponse<Payment> validatePayment(@RequestBody @Valid PayReq request) {
        return new BaseResponse<>(paymentService.validatePayment(request));
    }

    /**
     * 결제 취소 API
     * /resources/template/payment.html 참고!
     * [POST] /payments/cancel
     * @return BaseResponse<String>
     */
    @Operation(summary = "Post 결제 취소")
    @ApiResponse(responseCode = "200", description = "결제 취소 성공")
    @ApiResponse(responseCode = "400", description = "결제 취소 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("/cancel")
    public BaseResponse<String> cancelPayment(@RequestParam String imp_uid) {
        paymentService.cancelPayment(imp_uid);
        String response = imp_uid+" 결제 취소 완료";
        return new BaseResponse<>(response);
    }

}
