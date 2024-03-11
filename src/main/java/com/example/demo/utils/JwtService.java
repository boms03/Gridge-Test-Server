package com.example.demo.utils;


import com.example.demo.common.exceptions.BaseException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

import static com.example.demo.common.response.BaseResponseStatus.EMPTY_JWT;
import static com.example.demo.common.response.BaseResponseStatus.INVALID_JWT;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;

    /*
    JWT 생성
    @param userId
    @return String
     */
    public String createJwt(Long userId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 Authorization 으로 JWT 추출
    @return String
     */
    public String getJwt(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    /*
    JWT에서 userId 추출
    @return Long
    @throws BaseException
     */
    public Long getUserId(HttpServletRequest request) throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt(request);
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userId",Long.class);
    }

    public boolean validateToken(HttpServletRequest request) {
        String accessToken = getJwt(request);
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(accessToken);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token is invalid");
            return false;
        }
        return true;
    }




}
