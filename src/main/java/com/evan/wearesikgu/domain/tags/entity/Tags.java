package com.evan.wearesikgu.domain.tags.entity;

import com.evan.wearesikgu.domain.colors.Color;
import com.evan.wearesikgu.domain.project.entity.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @NotNull(message = "태그 이름은 필수입니다.")
    @Column(unique = true)
    @Length(max = 50)
    private String name;
}
