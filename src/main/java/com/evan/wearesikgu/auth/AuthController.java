package com.evan.wearesikgu.auth;

import com.evan.wearesikgu.auth.oauth.OAuthUserInfo;
import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.util.CookieUtil;
import com.evan.wearesikgu.config.token.TokenResponse;
import com.evan.wearesikgu.config.token.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public BaseResponse<Object> login(@RequestBody OAuthUserInfo userInfo) {
        TokenResponse jwt = authService.login(userInfo);
        return new BaseResponse<>(jwt);
    }

    @PostMapping("/reissue")
    public BaseResponse<Object> reissue(
            @CookieValue String accessToken,
            @CookieValue String refreshToken) {
        String newAccessToken = tokenService.reissueAccessToken(accessToken, refreshToken);
        return new BaseResponse<>(newAccessToken);
    }

    @PostMapping("/logout")
    public BaseResponse<Object> logout(
            @CookieValue String accessToken,
            @CookieValue String refreshToken,
            HttpServletResponse response) {
        tokenService.deleteRefreshToken(accessToken, refreshToken);
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
