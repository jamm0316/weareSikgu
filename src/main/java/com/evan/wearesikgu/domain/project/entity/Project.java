package com.evan.wearesikgu.domain.project.entity;

import com.evan.wearesikgu.common.enums.Status;
import com.evan.wearesikgu.common.enums.Visibility;
import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.colors.Color;
import com.evan.wearesikgu.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
public class Project extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "member_id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "color_id는 필수 입니다.")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @NotNull(message = "프로젝트 이름은 필수 입니다.")
    private String name;

    @NotNull(message = "상태값은 필수 입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'SCHEDULE'")
    @Length(max = 10)
    private Status status;

    private String startDate;

    private String endDate;

    private String actualEndDate;

    private String description;

    @NotNull(message = "공개 여부는 필수입니다.")
    @Column(columnDefinition = "boolean default true")
    private Boolean isPublic;

    @NotNull(message = "공개 범위는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'PRIVATE'")
    @Length(max = 20)
    private Visibility visibility;
}
