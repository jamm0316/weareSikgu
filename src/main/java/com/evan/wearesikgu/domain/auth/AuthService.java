package com.evan.wearesikgu.domain.auth;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.config.security.jwt.JwtProvider;
import com.evan.wearesikgu.domain.auth.kakao.KaKaoOAuthService;
import com.evan.wearesikgu.domain.auth.kakao.KakaoUserInfoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private KaKaoOAuthService kaKaoOAuthService;

    @Autowired
    private JwtProvider jwtProvider;

    public BaseResponse<String> kakaoLogin(String code) {
        String oauthAccessToken = kaKaoOAuthService.getAccessToken(code);
        KakaoUserInfoResponseDTO userInfo = kaKaoOAuthService.getUserInfo(oauthAccessToken);
        String apiAccessToken = jwtProvider.generateToken(userInfo.getId() + "");
        return new BaseResponse<>(apiAccessToken);
    }
}
