package com.example.demo.common.oauth;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.model.GoogleOAuthToken;
import com.example.demo.src.user.model.GoogleUser;
import com.example.demo.src.user.model.KakaoOAuthToken;
import com.example.demo.src.user.model.KakaoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.common.response.BaseResponseStatus.KAKAO_ERROR;
import static com.example.demo.common.response.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth{

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String KAKAO_SNS_AUTHORIZATION_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_SNS_TOKEN_URL;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_SNS_USER_INFO_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_SNS_REDIRECT_URI;

    private final ObjectMapper objectMapper;

    public ResponseEntity<String> requestAccessToken(String code) {
        String accessToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , KAKAO_SNS_CLIENT_ID);
            params.add("code"         , code);
            params.add("redirect_uri" , KAKAO_SNS_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_SNS_TOKEN_URL,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            log.info("response.getBody() = {}", response.getBody());

            if(response.getStatusCode() == HttpStatus.OK){
                return response;
            }

        } catch (Exception e) {
            throw new BaseException(KAKAO_ERROR);
        }

        return null;
    }

    public KakaoOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() = {}", response.getBody());

        KakaoOAuthToken kakaoOAuthToken= objectMapper.readValue(response.getBody(),KakaoOAuthToken.class);
        return kakaoOAuthToken;

    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_SNS_USER_INFO_URL,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        log.info("response.getBody() = {}", response.getBody());

        return response;
    }

    public KakaoUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException{
        String responseBody = userInfoRes.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();


        return new KakaoUser(id, nickname, email);
    }


    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_REDIRECT_URI);

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = KAKAO_SNS_AUTHORIZATION_URL + "?" + parameterString;
        log.info("redirectURL = ", redirectURL);

        return redirectURL;
    }
}
