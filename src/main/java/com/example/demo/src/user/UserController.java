package com.example.demo.src.user;


import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.oauth.KakaoOauth;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final OAuthService oAuthService;

    private final JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/auth/signup")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 전화번호 정규식
        if(!isRegexPhoneNumber(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        // 전화번호 중복 검사
        if(userService.checkUserByPhoneNumber(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_EXISTS_PHONE);
        }
        // 전화번호 길이 검사
        if(postUserReq.getPhoneNumber().length()>20){
            postUserReq.setPhoneNumber(postUserReq.getPhoneNumber().substring(0,19));
        }
        //아이디 정규식
        if(!isRegexUserName(postUserReq.getUsername())){
            return new BaseResponse<>(POST_USERS_INVALID_USERNAME);
        }
        //아이디 중복 검사
        if(userService.checkUserByUserName(postUserReq.getUsername())){
            return new BaseResponse<>(POST_USERS_EXISTS_USERNAME);
        }
        // 아이디 길이 검사
        if(postUserReq.getUsername().length()>20){
            postUserReq.setUsername(postUserReq.getUsername().substring(0,19));
        }
        // 비밀번호 정규식
        if(!isRegexPassword(postUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        //이름 길이 검사
        if(postUserReq.getName().length()>20){
            postUserReq.setName(postUserReq.getName().substring(0,19));
        }
        //약관 동의 검사
        if(!postUserReq.isTermsOfService() || !postUserReq.isDataBasedPolicy() || !postUserReq.isLocationBasedPolicy()){
            return new BaseResponse<>(POST_USERS_INVALID_TERMS);
        }

        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /app/users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        if(Email == null){
            List<GetUserRes> getUsersRes = userService.getUsers();
            return new BaseResponse<>(getUsersRes);
        }
        // Get Users
        List<GetUserRes> getUsersRes = userService.getUsersByEmail(Email);
        return new BaseResponse<>(getUsersRes);
    }

    /**
     * 회원 1명 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") Long userId) {
        GetUserRes getUserRes = userService.getUser(userId);
        return new BaseResponse<>(getUserRes);
    }



    /**
     * 유저정보변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq, HttpServletRequest request){

        Long jwtUserId = jwtService.getUserId(request);

        userService.modifyUserName(userId, patchUserReq);

        String result = "수정 완료!!";
        return new BaseResponse<>(result);

    }

    /**
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId, HttpServletRequest request){
        Long jwtUserId = jwtService.getUserId(request);

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/auth/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
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
    @GetMapping("/auth/{socialLoginType}/login")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.accessRequest(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @GetMapping("/login/kakao")
    public BaseResponse<GetSocialOAuthRes> kakao(
            @RequestParam(name= "code") String code
    ) throws IOException {
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(SocialLoginType.KAKAO,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }
}
