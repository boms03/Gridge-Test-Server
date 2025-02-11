package com.example.demo.src.follow;

import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/follows")
public class FollowController {

    private final JwtService jwtService;
    private final FollowService followService;

    /**
     * 팔로우 추가 API
     * [POST] /follows/{followeeUsername}
     * @param followeeUsername 팔로잉할 상대방 아이디
     * @return BaseResponse<String>
     */
    @Operation(summary = "Post 팔로우")
    @ApiResponse(responseCode = "200", description = "팔로우 성공")
    @ApiResponse(responseCode = "400", description = "팔로우 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PostMapping("{followeeUsername}")
    public BaseResponse<String> createFollow(
            @Parameter(required = true, description = "팔로잉할 상대방 아이디")
            @PathVariable String followeeUsername,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        followService.createFollow(followeeUsername,id);
        String response = "팔로우 완료";
        return new BaseResponse<>(response);
    }


    /**
     * 팔로우 취소 API
     * [DELETE] /follows/{followeeUsername}
     * @param followeeUsername 언팔로잉할 상대방 아이디
     * @return BaseResponse<String>
     */
    @Operation(summary = "Delete 팔로우 취소")
    @ApiResponse(responseCode = "200", description = "팔로우 취소 성공")
    @ApiResponse(responseCode = "400", description = "팔로우 취소 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @DeleteMapping("{followeeUsername}")
    public BaseResponse<String> deleteFollow(
            @Parameter(required = true, description = "언팔로잉할 상대방 아이디")
            @PathVariable String followeeUsername,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        followService.deleteFollow(followeeUsername,id);
        String response = "팔로우 취소 완료";
        return new BaseResponse<>(response);
    }
}
