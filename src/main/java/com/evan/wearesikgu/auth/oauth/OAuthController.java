package com.evan.wearesikgu.auth.oauth;

import com.evan.wearesikgu.auth.AuthService;
import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.common.util.CookieUtil;
import com.evan.wearesikgu.config.token.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthServiceFactory oAuthServiceFactory;
    private final AuthService authService;

    @GetMapping("login/{provider}")
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse response) throws IOException {
        OAuthService oAuthService = oAuthServiceFactory.getService(provider);
        String authUrl = oAuthService.buildAuthorizationUrl(provider);
        response.sendRedirect(authUrl);
    }

    @GetMapping("callback/{provider}")
    public BaseResponse<Object> handleCallback(
            @PathVariable String provider,
            @RequestParam String code,
            HttpServletResponse response) {

        OAuthService oAuthService = oAuthServiceFactory.getService(provider);
        String accessToken = oAuthService.getAccessToken(code);
        OAuthUserInfo userInfo = oAuthService.getUserInfo(accessToken);

        TokenResponse tokenResponse = authService.login(userInfo);

        CookieUtil.addTokenCookies(response, tokenResponse);

        return new BaseResponse<>(tokenResponse);
    }
}
