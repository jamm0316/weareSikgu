package com.evan.wearesikgu.auth.oauth;

public interface OAuthService {
    OAuth2Provider getProvider();

    /** 인가코드로 부터 액세스 토큰을 발급 받아 반환 */
    String getAccessToken(String code);

    /** 엑세스 토큰으로부터 프로바이더별 사용자 정보 DTO 반환 */
    OAuthUserInfo getUserInfo(String accessToken);

    String buildAuthorizationUrl(String providerName);
}
