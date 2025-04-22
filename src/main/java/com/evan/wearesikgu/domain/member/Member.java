package com.evan.wearesikgu.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "이메일은 필수입니다.")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profileImageUrl;

    @NotNull(message = "이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "별명은 필수입니다.")
    @Column(nullable = false)
    private String nickName;

    @Column(length = 13, unique = true)
    private String phoneNumber;
}
