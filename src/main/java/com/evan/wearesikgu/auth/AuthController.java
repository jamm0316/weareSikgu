package com.evan.wearesikgu.auth;

import com.evan.wearesikgu.auth.config.OAuth2Properties;
import com.evan.wearesikgu.auth.oauth.OAuth2Provider;
import com.evan.wearesikgu.auth.oauth.OAuthService;
import com.evan.wearesikgu.auth.oauth.OAuthServiceFactory;
import com.evan.wearesikgu.auth.oauth.OAuthUserInfo;
import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody OAuthUserInfo userInfo) {
        String jwt = authService.login(userInfo);
        return new BaseResponse<>(jwt);
    }
}
