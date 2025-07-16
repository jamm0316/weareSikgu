package com.evan.wearesikgu.domain.member;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/signup")
    public BaseResponse<Object> signUp(@RequestBody @Valid SignupRequestDTO signupDTO) {
        SignupResponseVO signupResponseDTO = memberService.save(signupDTO);
        return new BaseResponse<>(signupResponseDTO);
    }

    @GetMapping("/mypage")
    public BaseResponse<Object> userInfo(@AuthenticationPrincipal User user) {
        return new BaseResponse<>(user);
    }
}
