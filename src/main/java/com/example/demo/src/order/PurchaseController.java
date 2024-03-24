package com.example.demo.src.order;

import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.order.model.PurchaseReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaserService purchaserService;


    /**
     * 결제 후 주문 정보 기록 API
     * /resources/template/payment.html 참고!
     * [POST] /purchases/success
     * @return BaseResponse<String>
     */
    @Operation(summary = "Post 주문 정보 기록")
    @ApiResponse(responseCode = "200", description = "주문 정보 기록 성공")
    @ApiResponse(responseCode = "400", description = "주문 정보 기록 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("/success")
    public BaseResponse<String> saveOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveOrder(request);
        String response = "주문 성공 기록  성공";
        return new BaseResponse<>(response);
    }


    /**
     * 결제 중 실패한 주문 정보 기록 API
     * /resources/template/payment.html 참고!
     * [POST] /purchases/fail
     * @return BaseResponse<String>
     */
    @Operation(summary = "Post 주문 실패 정보 기록")
    @ApiResponse(responseCode = "200", description = "주문 실패 정보 기록 성공")
    @ApiResponse(responseCode = "400", description = "주문 실패 정보 기록 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("/fail")
    @ResponseBody
    public BaseResponse<String> saveFailOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveFailOrder(request);
        String response = "주문 실패 기록 성공";
        return new BaseResponse<>(response);
    }


}