package com.example.demo.common.oauth;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.demo.common.response.BaseResponseStatus.INVALID_OAUTH_TYPE;
import static com.example.demo.common.response.BaseResponseStatus.SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;
    private final UserService userService;
    private final JwtService jwtService;


    public void accessRequest(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){ //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
            case KAKAO:{
                redirectURL= kakaoOauth.getOauthRedirectURL();
            }break;
            default:{
                throw new BaseException(INVALID_OAUTH_TYPE);
            }

        }

        response.sendRedirect(redirectURL);
    }


    public GetSocialOAuthRes oAuthLoginOrJoin(Constant.SocialLoginType socialLoginType, String code) {

        switch (socialLoginType) {
            case KAKAO: {

                try {
                    // 엑세스 토큰 받기
                    ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);

                    // 응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                    KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);

                    //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                    ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);

                    //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                    KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);

                    //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
                    if(userService.checkUserByEmail(kakaoUser.getEmail())) { // user가 DB에 있다면, 로그인 진행
                        // 유저 정보 조회
                        GetUserRes getUserRes = userService.getUserByEmail(kakaoUser.getEmail());

                        //서버에 user가 존재하면 앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
                        String jwtToken = jwtService.createJwt(getUserRes.getId(), String.valueOf(getUserRes.getRole()));

                        //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
                        GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, getUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                        return getSocialOAuthRes;
                    }else { // user가 DB에 없다면, 회원가입 진행
                        // 유저 정보 저장
                        PostUserRes postUserRes = userService.createOAuthUser(kakaoUser.toEntity());
                        GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(postUserRes.getJwt(), postUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                        return getSocialOAuthRes;
                    }

                } catch (IOException e){
                    throw new BaseException(SERVER_ERROR);
                }

            }

            default: {
                throw new BaseException(INVALID_OAUTH_TYPE);
            }

        }
    }


}
