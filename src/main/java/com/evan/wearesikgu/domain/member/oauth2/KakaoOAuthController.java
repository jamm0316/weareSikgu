package com.evan.wearesikgu.domain.member.oauth2;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth/kakao")
public class KakaoOAuthController {
    @Autowired
    OAuthService oAuthService;

    @GetMapping("/callback")
    public BaseResponse<Object> callback(@RequestParam String code) {
        String accessToken = oAuthService.getAccessToken(code);
        KakaoUserInfoResponseDTO userInfo = getUserInfo(accessToken);
        return new BaseResponse<>(userInfo);
    }

    private KakaoUserInfoResponseDTO getUserInfo(String accessToken) {
        return oAuthService.getUserInfo(accessToken);
    }
}
