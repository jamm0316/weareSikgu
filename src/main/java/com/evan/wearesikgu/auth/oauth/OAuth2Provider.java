package com.evan.wearesikgu.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OAuth2Provider {
    KAKAO("https://kauth.kakao.com/oauth/authorize"),
    GOOGL(""),
    NAVER("");

    private final String authorizeUrl;
}