package com.evan.wearesikgu.domain.member.oauth2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth/kakao")
public class KakaoOAuth2Controller {
    //todo: AccessToken 발급 절차 구현하기
    @GetMapping("/callback")
    public String callback(@RequestParam String code) {
        return "요청성공{code: " + code + "}";
    }
}
