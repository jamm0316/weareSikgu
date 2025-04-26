package com.evan.wearesikgu.domain.food.entity;

import com.evan.wearesikgu.common.superentity.SuperEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BigCategory extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "대분류 이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String bigCategoryImage;

}
