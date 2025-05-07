package com.evan.wearesikgu.domain.member.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    @Autowired
    private KakaoOAuthClient kakaoOAuthClient;

    public String getAccessToken(String code) {
        return kakaoOAuthClient.requestAccessToken(code);
    }

    public KakaoUserInfoResponseDTO getUserInfo(String accessToken) {
        return kakaoOAuthClient.requestUserInfo(accessToken);
    }
}
