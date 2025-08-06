package com.evan.wearesikgu.domain.task.entity;

import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.project.entity.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Task extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "프로젝트 id는 필수 입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String description;

    private LocalDateTime due_date;
}
