package com.example.demo.src.subscription;

import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final JwtService jwtService;
    private final SubscriptionService subscriptionService;

    /**
     * 구독 버튼 클릭시 구독 여부 체크 API
     * [GET] /subscription/check
     * @return BaseResponse<String>
     */
    @Operation(summary = "Get 구독 체크")
    @ApiResponse(responseCode = "200", description = "구독 체크 성공")
    @ApiResponse(responseCode = "400", description = "이미 구독한 계정", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/check")
    public BaseResponse<String> checkSubscription(
            HttpServletRequest request
    ){
        Long userId = jwtService.getUserId(request);
        subscriptionService.subscribe(userId);
        String response = "구독 여부 확인 완료";
        return new BaseResponse<>(response);
    }

    /**
     * 구독 취소 API
     * [PUT] /subscription
     * @return BaseResponse<String>
     */
    @Operation(summary = "Put 구독 취소")
    @ApiResponse(responseCode = "200", description = "구독 취소 성공")
    @ApiResponse(responseCode = "400", description = "구독 상태가 아닌 계정 구독 취소 실패" , content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
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
