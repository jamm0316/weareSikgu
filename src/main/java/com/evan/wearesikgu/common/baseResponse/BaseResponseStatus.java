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

    /**
     * 4000: 서버 오류
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류입니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

