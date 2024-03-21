package com.example.demo.src.user;


import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.mapping.follow.FollowService;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final FollowService followService;

    private final UserService userService;

    private final OAuthService oAuthService;

    private final JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @Operation(summary = "Post 회원가입")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    @PostMapping("/auth/signup")
    public BaseResponse<PostUserRes> createUser(@RequestBody @Valid PostUserReq postUserReq) {
        // 전화번호 정규식
        if(!isRegexPhoneNumber(postUserReq.getPhoneNumber())){
            throw new BaseException(POST_USERS_INVALID_PHONE);
        }
        // 전화번호 중복 검사
        if(userService.checkUserByPhoneNumber(postUserReq.getPhoneNumber())){
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }
        // 전화번호 길이 검사
        if(postUserReq.getPhoneNumber().length()>20){
            postUserReq.setPhoneNumber(postUserReq.getPhoneNumber().substring(0,19));
        }
        //아이디 정규식
        if(!isRegexUserName(postUserReq.getUsername())){
            throw new BaseException(POST_USERS_INVALID_USERNAME);
        }
        //아이디 중복 검사
        if(userService.checkUserByUserName(postUserReq.getUsername())){
            throw new BaseException(POST_USERS_EXISTS_USERNAME);
        }
        // 아이디 길이 검사
        if(postUserReq.getUsername().length()>20){
            postUserReq.setUsername(postUserReq.getUsername().substring(0,19));
        }
        // 비밀번호 정규식
        if(!isRegexPassword(postUserReq.getPassword())){
            throw new BaseException(POST_USERS_INVALID_PASSWORD);
        }
        //이름 길이 검사
        if(postUserReq.getName().length()>20){
            postUserReq.setName(postUserReq.getName().substring(0,19));
        }
        //약관 동의 검사
        if(!postUserReq.isTermsOfService() || !postUserReq.isDataBasedPolicy() || !postUserReq.isLocationBasedPolicy()){
            throw new BaseException(POST_USERS_INVALID_TERMS);
        }

        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }

    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @Operation(summary = "Post 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    @PostMapping("/auth/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody @Valid PostLoginReq postLoginReq){
        //아이디 정규식
        if(!isRegexUserName(postLoginReq.getUsername())){
            return new BaseResponse<>(POST_USERS_INVALID_USERNAME);
        }

        //비밀번호 정규식
        if(!isRegexPassword(postLoginReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /auth/:socialLoginType/login
     * @return void
     */
    @Operation(summary = "Get 소셜 로그인 리다이렉트")
    @ApiResponse(responseCode = "200", description = "리다이렉트 성공")
    @ApiResponse(responseCode = "400", description = "리다이렉트 실패")
    @GetMapping("/auth/{socialLoginType}/login")
    public void socialLoginRedirect(
            @Parameter(required = true, description = "소셜 로그인 타입")
            @PathVariable(name="socialLoginType") String SocialLoginPath
    ) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.accessRequest(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @Operation(summary = "Get 소셜 로그인")
    @ApiResponse(responseCode = "200", description = "소셜 로그인 성공")
    @ApiResponse(responseCode = "400", description = "소셜 로그인 실패")
    @GetMapping("/login/kakao")
    public BaseResponse<GetSocialOAuthRes> kakao(
            @Parameter(required = true, description = "Oauth 인가 코드")
            @RequestParam(name= "code") String code
    ) {
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(SocialLoginType.KAKAO,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }

    @Operation(summary = "Post 팔로우")
    @ApiResponse(responseCode = "200", description = "팔로우 성공")
    @ApiResponse(responseCode = "400", description = "팔로우 실패")
    @PostMapping("/follow")
    public BaseResponse<String> createFollow(
            @Parameter(required = true, description = "팔로잉할 상대방 아이디")
            @RequestParam String followeeUsername,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        followService.createFollow(followeeUsername,id);
        String response = "팔로우 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Delete 팔로우 취소")
    @ApiResponse(responseCode = "200", description = "팔로우 취소 성공")
    @ApiResponse(responseCode = "400", description = "팔로우 취소 실패")
    @DeleteMapping("/follow")
    public BaseResponse<String> deleteFollow(
            @Parameter(required = true, description = "언팔로잉할 상대방 아이디")
            @RequestParam String followeeUsername,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        followService.deleteFollow(followeeUsername,id);
        String response = "팔로우 취소 완료";
        return new BaseResponse<>(response);
    }
}
