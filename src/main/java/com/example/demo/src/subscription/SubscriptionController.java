package com.example.demo.src.subscription;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    private final JwtService jwtService;
    private final SubscriptionService subscriptionService;

    @Operation(summary = "Get 구독 체크")
    @ApiResponse(responseCode = "200", description = "구독 체크 성공")
    @ApiResponse(responseCode = "400", description = "구독 체크 실패")
    @GetMapping("")
    public BaseResponse<String> subscribe(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        subscriptionService.subscribe(userId);
        String response = "구독 여부 체크 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Put 구독 취소")
    @ApiResponse(responseCode = "200", description = "구독 취소 성공")
    @ApiResponse(responseCode = "400", description = "구독 취소 실패")
    @PutMapping("")
    public BaseResponse<String> unsubscribe(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        subscriptionService.unsubscribe(userId);
        String response = "구독 취소 완료";
        return new BaseResponse<>(response);
    }
}
