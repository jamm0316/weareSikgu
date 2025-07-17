package com.evan.wearesikgu.auth.oauth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuthServiceFactory {
    private final List<OAuthService> services;
    private Map<OAuth2Provider, OAuthService> serviceMap;

    @PostConstruct
    private void init() {
        this.serviceMap = services.stream()
                .collect(Collectors.toMap(OAuthService::getProvider, Function.identity()));
    }

    public OAuthService getService(OAuth2Provider provider) {
        OAuthService oAuthService = serviceMap.get(provider);
        if (oAuthService == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider: " + provider);
        }
        return oAuthService;
    }

    /**
     * @Param provider 대문자 문자열: "KAKAO", "GOOGLE", "NAVER"
     */
    public OAuthService getService(String providerName) {
        try {
            OAuth2Provider provider = OAuth2Provider.valueOf(providerName.toUpperCase(Locale.ROOT));
            return getService(provider);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 provider 값: " + providerName, e);
        }
    }
}
