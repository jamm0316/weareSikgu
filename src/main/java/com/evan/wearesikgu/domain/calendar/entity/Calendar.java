package com.evan.wearesikgu.domain.calendar.entity;

import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Calendar  extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "캘린더 이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "초대 링크는 필수입니다.")
    @Column(length = 64, nullable = false, unique = true)
    private String inviteToken;

    @NotNull(message = "ownerId은 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;
}
