package com.evan.wearesikgu.domain.member;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/signup")
    public BaseResponse<Object> signUp(@RequestBody @Valid SignupRequestDTO signupDTO) {
        SignupResponseVO signupResponseDTO = memberService.save(signupDTO);
        return new BaseResponse<>(signupResponseDTO);
    }
}
