package com.evan.wearesikgu.common.baseResponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: 요청 오류
     */
    DUPLICATE_MEMBER(false, 2001, "이미 존재하는 회원입니다."),
    VALIDATION_ERROR(false, 2002, "요청 데이터가 유효하지 않습니다."),
    NOTFOUND_MEMBER(false, 2003, "회원이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(false, 2004, "유효하지 않거나 만료된 리프레시 토큰입니다."),

    /**
     * 3000: 외부 API 연동오류
     */
    KAKAO_REDIRECT_MISMATCH(false, 3001, "카카오 Redirect URI가 일치하지 않습니다."),

    /**
     * 4000: 서버 오류
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류입니다."),
    UNSUPPORTED_PROVIDER(false, 4001, "지원하지 않는 OAuth2 Provider 입니다."),

    /**
     * 5000: JWT 오류
     */
    TOKEN_INVALID_SIGNATURE(false, 5001, "JWT 서명이 유효하지 않습니다."),
    TOKEN_MALFORMED(false, 5002, "JWT 구조가 올바르지 않습니다."),
    TOKEN_EXPIRED(false, 5003, "JWT 토큰이 만료되었습니다."),
    TOKEN_UNSUPPORTED(false, 5004, "지원되지 않는 JWT 토큰입니다."),
    TOKEN_ILLEGAL_ARGUMENT(false, 5006, "JWT 클레임이 비어 있거나 잘못 되었습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

