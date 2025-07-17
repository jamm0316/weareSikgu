package com.evan.wearesikgu.auth.oauth.kakao.model;

import com.evan.wearesikgu.auth.config.OAuth2Properties;
import com.evan.wearesikgu.auth.oauth.OAuth2Provider;
import com.evan.wearesikgu.auth.oauth.OAuthService;
import com.evan.wearesikgu.auth.oauth.kakao.dtos.KakaoOAuthClient;
import com.evan.wearesikgu.auth.oauth.kakao.dtos.KakaoUserInfoDTO;
import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KaKaoOAuthService implements OAuthService {
    final private KakaoOAuthClient kakaoOAuthClient;
    final private OAuth2Properties oAuth2Properties;

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken(String code) {
        return kakaoOAuthClient.requestAccessToken(code);
    }

    @Override
    public KakaoUserInfoDTO getUserInfo(String accessToken) {
        return kakaoOAuthClient.requestUserInfo(accessToken);
    }

    @Override
    public String buildAuthorizationUrl(String providerName) {
        OAuth2Properties.Provider providerConfig = oAuth2Properties.getProviders().get(providerName);
        if (providerConfig == null) {
            throw new BaseException(BaseResponseStatus.UNSUPPORTED_PROVIDER);
        }

        return UriComponentsBuilder
                .fromHttpUrl(OAuth2Provider.KAKAO.getAuthorizeUrl())
                .queryParam("response_type", "code")
                .queryParam("client_id", providerConfig.getRestApiKey())
                .queryParam("redirect_uri", providerConfig.getRedirectUri())
                .build(true)
                .toUriString();
    }
}
