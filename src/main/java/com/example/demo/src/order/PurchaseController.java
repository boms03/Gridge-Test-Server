package com.example.demo.src.order;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.order.model.PurchaseReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaserService purchaserService;

    @PostMapping("/order/success")
    @ResponseBody
    public BaseResponse<String> saveOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveOrder(request);
        String response = "주문 성공 기록  성공";
        return new BaseResponse<>(response);
    }

    @PostMapping("/order/fail")
    @ResponseBody
    public BaseResponse<String> saveFailOrder(@RequestBody PurchaseReq request) {
        purchaserService.saveFailOrder(request);
        String response = "주문 실패 기록 성공";
        return new BaseResponse<>(response);
    }


}