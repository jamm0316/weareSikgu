package com.evan.wearesikgu.auth;

import com.evan.wearesikgu.auth.oauth.OAuthUserInfo;
import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
