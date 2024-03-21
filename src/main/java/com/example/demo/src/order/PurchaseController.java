package com.example.demo.src.order;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.order.model.PurchaseReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaserService purchaserService;

    @Operation(summary = "Post 주문 정보 기록")
    @ApiResponse(responseCode = "200", description = "주문 정보 기록 성공")
    @ApiResponse(responseCode = "400", description = "주문 정보 기록 실패")
    @PostMapping("/purchase/success")
    public BaseResponse<String> saveOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveOrder(request);
        String response = "주문 성공 기록  성공";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Post 주문 실패 정보 기록")
    @ApiResponse(responseCode = "200", description = "주문 실패 정보 기록 성공")
    @ApiResponse(responseCode = "400", description = "주문 실패 정보 기록 실패")
    @PostMapping("/purchase/fail")
    @ResponseBody
    public BaseResponse<String> saveFailOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveFailOrder(request);
        String response = "주문 실패 기록 성공";
        return new BaseResponse<>(response);
    }


}