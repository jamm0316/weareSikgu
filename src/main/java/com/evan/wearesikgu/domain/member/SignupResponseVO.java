package com.evan.wearesikgu.domain.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseVO {
    private String email;
    private String profileImageUrl;
    private String name;
    private String nickName;

    public SignupResponseVO(String email, String profileImageUrl, String name, String nickName) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.nickName = nickName;
    }
}
