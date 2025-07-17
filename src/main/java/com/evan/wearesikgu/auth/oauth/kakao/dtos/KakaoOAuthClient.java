package com.evan.wearesikgu.auth.oauth.kakao.dtos;

import com.evan.wearesikgu.auth.config.OAuth2Properties;
import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final RestTemplate restTemplate;
    private final OAuth2Properties oAuth2Properties;
//    @Value("${oauth2.providers.kakao.rest-api-key}")
//    private String clientId;
//
//    @Value("${oauth2.providers.kakao.redirect-uri}")
//    private String redirectUri;

    public String requestAccessToken(String code) {
        OAuth2Properties.Provider kakaoProvider = oAuth2Properties.getProviders().get("kakao");
        if (kakaoProvider == null) {
            throw new BaseException(BaseResponseStatus.UNSUPPORTED_PROVIDER);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProvider.getRestApiKey());
        params.add("redirect_uri", kakaoProvider.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponseDTO> response = restTemplate.postForEntity(
                    "https://kauth.kakao.com/oauth/token",
                    request,
                    KakaoTokenResponseDTO.class
            );

            return response.getBody().getAccessToken();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST &&
                    e.getResponseBodyAsString().contains("KOE303")) {
                throw new BaseException(BaseResponseStatus.KAKAO_REDIRECT_MISMATCH);
            }
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public KakaoUserInfoDTO requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoDTO> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoUserInfoDTO.class
        );

        return response.getBody();
    }
}
