package com.evan.wearesikgu.domain.auth;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("login/kakao")
    public BaseResponse<String> getAccessToken(@RequestParam String code) {
        return authService.kakaoLogin(code);
    }
}
