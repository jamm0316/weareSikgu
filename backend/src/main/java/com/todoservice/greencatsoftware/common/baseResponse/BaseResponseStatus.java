package com.todoservice.greencatsoftware.common.baseResponse;

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
    VALIDATION_ERROR(false, 2001, "요청 데이터가 유효하지 않습니다."),
    NOTFOUND_MEMBER(false, 2003, "회원이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(false, 2004, "유효하지 않거나 만료된 리프레시 토큰입니다."),

    /**
     * 3000: 서버 오류
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류입니다."),
    UNSUPPORTED_PROVIDER(false, 4001, "지원하지 않는 OAuth2 Provider 입니다."),

    /**
     * 4000: Member 오류
     */
    MISSING_EMAIL_FOR_MEMBER(false, 3001, "Member의 email은 필수입니다."),
    MISSING_PASSWORD_FOR_MEMBER(false, 3002, "Member의 password는 필수입니다."),
    MISSING_NAME_FOR_MEMBER(false, 3003, "Member의 name는 필수입니다."),
    NOT_FOUND_MEMBER(false, 3004, "유효하지 않은 멤버 입니다"),
    KAKAO_REDIRECT_MISMATCH(false, 3001, "카카오 Redirect URI가 일치하지 않습니다."),

    /**
     * 5000: JWT 오류
     */
    TOKEN_INVALID_SIGNATURE(false, 5001, "JWT 서명이 유효하지 않습니다."),
    TOKEN_MALFORMED(false, 5002, "JWT 구조가 올바르지 않습니다."),
    TOKEN_EXPIRED(false, 5003, "JWT 토큰이 만료되었습니다."),
    TOKEN_UNSUPPORTED(false, 5004, "지원되지 않는 JWT 토큰입니다."),
    TOKEN_ILLEGAL_ARGUMENT(false, 5006, "JWT 클레임이 비어 있거나 잘못 되었습니다."),

    /**
     * 6000: Project 오류
     */
    NOT_FOUND_PROJECT(false, 6001, "유효하지 않은 프로젝트 입니다"),
    END_DATE_BEFORE_START_DATE(false, 6002, "종료일은 시작일보다 빠를 수 없습니다."),
    ACTUAL_END_DATE_BEFORE_START_DATE(false, 6003, "실제 종료일은 시작일보다 빠를 수 없습니다."),
    MISSING_COLOR_FOR_PROJECT(false, 6004, "Project의 Color는 필수입니다."),
    MISSING_TITLE_FOR_PROJECT(false, 6005, "Project의 제목은 필수 입니다."),
    TITLE_EXCEEDS_LIMIT_FOR_PROJECT(false, 6006, "제목은 100자를 초과할 수 없습니다."),
    MISSING_STATUS_FOR_PROJECT(false, 6007, "Project는 상태값이 필수 입니다."),
    MISSING_IS_PUBLIC_FOR_PROJECT(false, 6008, "Project 공개 여부는 필수 입니다."),
    MISSING_VISIBILITY_FOR_PROJECT(false, 6009, "Project 공개 범위는 필수 입니다."),
    MISSING_MEMBER_FOR_PROJECT(false, 6010, "Project의 Member는 필수입니다."),


    /**
     * 7000: Task 오류
     */
    NOT_FOUND_TASK(false, 7001, "유효하지 않은 TASK 입니다"),
    MISSING_PROJECT_FOR_TASK(false, 7002, "Task는 반드시 Project에 속해야 합니다."),
    MISSING_TITLE_FOR_TASK(false, 7004, "Task는 제목은 필수 입니다."),
    TITLE_EXCEEDS_LIMIT_FOR_TASK(false, 7005, "제목은 100자를 초과할 수 없습니다."),
    MISSING_PRIORITY_FOR_TASK(false, 7006, "Task는 우선순위가 필수 입니다."),
    MISSING_STATUS_FOR_TASK(false, 7007, "Task는 상태값이 필수 입니다."),

    /**
     * 8000: Schedule 오류
     */
    INVALID_DATE_ORDER(false, 8001, "마감일은 시작일보다 이후여야 합니다"),
    MISSING_START_DATE_FOR_TIME(false, 8002, "시작 시간을 사용하려면 시작 날짜가 필요합니다"),
    MISSING_DUE_DATE_FOR_TIME(false, 8003, "마감 시간을 사용하려면 마감 날짜가 필요합니다"),
    MISSING_START_TIME_VALUE(false, 8004, "시작 시간이 활성화되면 시작 시간 값이 필요합니다"),
    MISSING_DUE_TIME_VALUE(false, 8005, "마감 시간이 활성화되면 마감 시간 값이 필요합니다"),
    INVALID_TIME_ORDER_SAME_DATE(false, 8006, "같은 날짜에서 마감 시간은 시작 시간보다 이후여야 합니다"),

    /**
     * 9000: Color 오류
     */
    MISSING_NAME_FOR_COLOR(false, 9001, "색상 이름은 필수입니다."),
    MISSING_HEX_CODE_FOR_COLOR(false, 9002, "핵사코드는 필수입니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
    }

