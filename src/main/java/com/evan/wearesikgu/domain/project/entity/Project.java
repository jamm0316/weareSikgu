package com.evan.wearesikgu.domain.project.entity;

import com.evan.wearesikgu.common.enums.Color;
import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Project extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "member_id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "이름은 필수 입니다.")
    private String name;

    @Enumerated(EnumType.STRING)
    private Color color;
}
