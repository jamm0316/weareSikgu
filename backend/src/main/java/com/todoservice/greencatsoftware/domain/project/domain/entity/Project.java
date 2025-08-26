package com.todoservice.greencatsoftware.domain.project.domain.entity;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.common.superEntity.SuperEntity;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "color_id는 필수 입니다.")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "color_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PROJECT_COLOR")
    )
    private Color color;

    @NotNull(message = "프로젝트 이름은 필수 입니다.")
    private String name;

    @NotNull(message = "상태값은 필수 입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) default 'PLANNING'")
    private Status status;

    @Embedded
    private Period period;

    private String description;

    @NotNull(message = "공개 여부는 필수입니다.")
    @Column(columnDefinition = "boolean default true")
    private Boolean isPublic;

    @NotNull(message = "공개 범위는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'PRIVATE'")
    private Visibility visibility;

    public Project(Color color,
                   String name,
                   Status status,
                   Period period,
                   String description,
                   Boolean isPublic,
                   Visibility visibility) {

        validateDomainInvariants(color, name, status, isPublic, visibility);

        this.color = color;
        this.name = name.trim();
        this.status = status;
        this.period = period;
        this.description = description != null ? description.trim() : null;
        this.isPublic = isPublic;
        this.visibility = visibility;
    }

    private void validateDomainInvariants(Color color,
                                         String name,
                                         Status status,
                                         Boolean isPublic,
                                         Visibility visibility) {
        if (color == null) {
            throw new BaseException(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT);
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT);
        }

        if (name.length() > 100) {
            throw new BaseException(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT);
        }

        if (status == null) {
            throw new BaseException(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT);
        }

        if (isPublic == null) {
            throw new BaseException(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT);
        }

        if (visibility == null) {
            throw new BaseException(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT);
        }
    }

    public static Project create(Color color, String name, Status status,
                                 String description, Boolean isPublic, Visibility visibility) {
        return new Project(color, name, status, null, description, isPublic, visibility);
    }

    public static Project createWithPeriod(Color color, String name, Status status,
                                           Period period, String description, Boolean isPublic, Visibility visibility) {
        return new Project(color, name, status, period, description, isPublic, visibility);
    }

    public void changeColor(Color color) {
        if (color == null) {
            throw new BaseException(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT);
        }
        this.color = color;
    }

    public void changeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT);
        }

        if (name.length() > 100) {
            throw new BaseException(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT);
        }

        this.name = name.trim();
    }

    public void changeStatus(Status status) {
        if (status == null) {
            throw new BaseException(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT);
        }
        this.status = status;
    }

    public void changePeriod(Period period) {
        if (period.isNull()) {
            this.period = Period.noPeriod();
        } else {
            this.period = period;
        }
    }

    public void changeDescription(String description) {
        if (description != null && !description.isBlank()) {
            this.description = description.trim();
        } else {
            this.description = null;
        }
    }

    public void changeIsPublic(Boolean isPublic) {
        if (isPublic == null) {
            throw new BaseException(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT);
        }
        this.isPublic = isPublic;
    }

    public void changeVisibility(Visibility visibility) {
        if (visibility == null) {
            throw new BaseException(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT);
        }
        this.visibility = visibility;
    }
}
