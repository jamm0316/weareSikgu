package com.evan.wearesikgu.domain.auth.kakao.dtos;

import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kakao.rest-api-key}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
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

    public KakaoUserInfoResponseDTO requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponseDTO> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoUserInfoResponseDTO.class
        );

        return response.getBody();
    }
}
