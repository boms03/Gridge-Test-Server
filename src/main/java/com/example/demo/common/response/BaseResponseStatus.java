package com.example.demo.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */

    USERS_EMPTY_PHONE(false, HttpStatus.BAD_REQUEST.value(), "전화번호를 입력해주세요."),
    USERS_EMPTY_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일을 입력해주세요."),
    TEST_EMPTY_COMMENT(false, HttpStatus.BAD_REQUEST.value(), "코멘트를 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,HttpStatus.BAD_REQUEST.value(),"중복된 이메일입니다."),
    POST_USERS_EXISTS_PHONE(false,HttpStatus.BAD_REQUEST.value(),"중복된 전화번호입니다."),
    POST_USERS_EXISTS_USERNAME(false,HttpStatus.BAD_REQUEST.value(),"중복된 아이디입니다."),
    POST_USERS_INVALID_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "비밀번호 형식을 확인해주세요."),
    POST_USERS_INVALID_USERNAME(false, HttpStatus.BAD_REQUEST.value(), "아이디 형식을 확인해주세요."),
    POST_USERS_INVALID_PHONE(false, HttpStatus.BAD_REQUEST.value(), "전화번호 형식을 확인해주세요."),
    POST_USERS_INVALID_TERMS(false, HttpStatus.BAD_REQUEST.value(), "필수 약관 동의 항목을 확인해주세요."),

    POST_TEST_EXISTS_MEMO(false,HttpStatus.BAD_REQUEST.value(),"중복된 메모입니다."),

    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    DUPLICATED_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    INVALID_MEMO(false,HttpStatus.NOT_FOUND.value(), "존재하지 않는 메모입니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 아이디거나 비밀번호가 틀렸습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    INVALID_OAUTH_TYPE(false, HttpStatus.BAD_REQUEST.value(), "알 수 없는 소셜 로그인 형식입니다."),
    KAKAO_ERROR(false, HttpStatus.BAD_REQUEST.value(), "카카오 토큰 오류입니다."),

    ACCESS_DENIED(false, HttpStatus.FORBIDDEN.value(), "접근할 수 없는 계정입니다"),
    BANNED_USER(false, HttpStatus.FORBIDDEN.value(), "정지된 계정입니다"),
    WITHDRAW_USER(false, HttpStatus.FORBIDDEN.value(), "탈퇴한 계정입니다"),
    RENEW_USER(false, HttpStatus.FORBIDDEN.value(), "이용약관 재동의가 필요한 계정입니다"),

    EMPTY_FILE(false, HttpStatus.BAD_REQUEST.value(), "업로드된 파일이 없습니다"),
    LIMIT_FILE(false, HttpStatus.BAD_REQUEST.value(), "업로드된 파일이 10개 초과입니다"),

    NOT_FIND_BOARD(false,HttpStatus.NOT_FOUND.value(),"일치하는 게시물이 없습니다."),
    NOT_FIND_IMAGE(false, HttpStatus.NOT_FOUND.value(), "일치하는 이미지가 없습니다"),
    NOT_FIND_FOLLOW(false, HttpStatus.NOT_FOUND.value(), "일치하는 팔로우 관계가 없습니다"),
    NOT_FIND_COMMENT(false, HttpStatus.NOT_FOUND.value(), "일치하는 댓글이 없습니다"),
    NOT_FIND_REPORT(false, HttpStatus.NOT_FOUND.value(), "일치하는 신고 카테고리가 없습니다"),
    NOT_FIND_BOARD_REPORT(false, HttpStatus.NOT_FOUND.value(), "게시물 신고가 없습니다"),
    NOT_FIND_PREPAYMENT(false, HttpStatus.NOT_FOUND.value(), "결제 사전 검증이 없습니다"),
    NOT_FIND_SUBSCRIPTION(false,HttpStatus.NOT_FOUND.value(),"일치하는 구독이 없습니다."),

    OWN_REPORT(false, HttpStatus.BAD_REQUEST.value(), "자신의 게시물을 신고할 수 없습니다"),
    DUPLICATE_REPORT(false, HttpStatus.BAD_REQUEST.value(), "이미 신고한 게시물 입니다"),

    INVALID_LENGTH_COMMENT(false, HttpStatus.BAD_REQUEST.value(), "댓글은 1자 이상 2200자 이하 이어야 합니다"),
    INVALID_LENGTH_BOARD(false, HttpStatus.BAD_REQUEST.value(), "게시물 본문은 1자 이상 2200자 이하 이어야 합니다"),

    NOT_SUBSCRIBED(false, HttpStatus.BAD_REQUEST.value(), "구독된 계정이 아닙니다"),
    ALREADY_SUBSCRIBED(false, HttpStatus.BAD_REQUEST.value(), "이미 구독된 계정입니다"),


    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),

    MODIFY_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    DELETE_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저 삭제 실패"),
    MODIFY_FAIL_MEMO(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"메모 수정 실패"),

    UPLOAD_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 업로드 실패하였습니다"),

    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다."),

    IAMPORT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "아임포트 서버 에러가 발생했습니다");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
