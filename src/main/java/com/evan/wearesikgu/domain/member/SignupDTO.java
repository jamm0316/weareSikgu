package com.evan.wearesikgu.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDTO {
    private Long id;
    private String email;
    private String password;
    private String profileImageUrl;
    private String name;
    private String nickName;
    private String phoneNumber;
}