package com.evan.wearesikgu.domain.task.entity;

import com.evan.wearesikgu.common.enums.DayLabel;
import com.evan.wearesikgu.common.enums.Priority;
import com.evan.wearesikgu.common.enums.Status;
import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.colors.Color;
import com.evan.wearesikgu.domain.project.entity.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "프로젝트 id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull(message = "color는 필수 입니다.")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @NotNull(message = "priority는 필수 입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'HIGH'")
    private Priority priority;

    @NotNull(message = "제목은 필수입니다.")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private DayLabel day;

    private LocalDate startDate;

    private LocalDateTime startTime;

    @NotNull(message = "시작 시간 사용 여부는 필수입니다.")
    @Column(columnDefinition = "boolean default false")
    private Boolean startTimeEnabled;

    private LocalDate dueDate;

    private LocalDateTime dueTime;

    @NotNull(message = "마감 시간 사용 여부는 필수입니다.")
    @Column(columnDefinition = "boolean default false")
    private Boolean dueTimeEnabled;

    @NotNull(message = "상태는 필수 입니다.")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'SCHEDULE'")
    @Length(max = 10)
    private Status status;
}
