package com.todoservice.greencatsoftware.domain.member.domain.entity;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "이메일은 필수입니다.")
    @Column(unique = true, nullable = false)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    //    @Enumerated(EnumType.STRING)
//    private OAuth2Provider provider;

    private String providerId;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Column(nullable = false)
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    private String profileImageUrl;

    @NotNull(message = "이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    public Member(String email, String password, String profileImageUrl, String name) {
        validateDomainInvariants(email, password, profileImageUrl, name);

        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
    }

    private void validateDomainInvariants(String email, String password, String profileImageUrl, String name) {
        if (email == null || email.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER);
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER);
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_NAME_FOR_MEMBER);
        }
    }

    public static Member create(String email, String password, String profileImageUrl, String name) {
        return new Member(email, password, profileImageUrl, name);
    }

    public void changeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_EMAIL_FOR_MEMBER);
        }
        this.email = email;
    }

    public void changePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_PASSWORD_FOR_MEMBER);
        }
        this.password = password;
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void changeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_NAME_FOR_MEMBER);
        }
        this.name = name;
    }
}
