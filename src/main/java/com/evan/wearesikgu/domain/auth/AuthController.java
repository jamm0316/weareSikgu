package com.evan.wearesikgu.domain.auth;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Value("${kakao.rest-api-key}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("login/kakao")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .build(true)
                .toUriString();

        response.sendRedirect(kakaoAuthUrl);
    }

    @GetMapping("login/kakao/callback")
    public BaseResponse<String> getAccessToken(@RequestParam String code) {
        return authService.kakaoLogin(code);
    }
}
