package com.evan.wearesikgu.auth.oauth;

import com.evan.wearesikgu.auth.AuthService;
import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.common.util.CookieUtil;
import com.evan.wearesikgu.common.util.FingerprintUtil;
import com.evan.wearesikgu.config.token.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            HttpServletResponse response) {

        OAuthService oAuthService = oAuthServiceFactory.getService(provider);
        String accessToken = oAuthService.getAccessToken(code);
        OAuthUserInfo userInfo = oAuthService.getUserInfo(accessToken);

        String uaHash = FingerprintUtil.uaHash(request.getHeader("User-Agent"));
        String ipPrefix = FingerprintUtil.ipPrefix(FingerprintUtil.extractClientIp(request));

        TokenResponse tokenResponse = authService.login(userInfo, uaHash, ipPrefix);

        CookieUtil.addTokenCookies(response, tokenResponse);

        return new BaseResponse<>(tokenResponse);
    }
}
