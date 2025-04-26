package com.evan.wearesikgu.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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