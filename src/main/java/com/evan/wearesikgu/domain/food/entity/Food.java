package com.evan.wearesikgu.domain.food.entity;

import com.evan.wearesikgu.common.superentity.SuperEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Food  extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "이름은 필수입니다.")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String imageUrl;

    @NotNull(message = "대분류는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "big_category_id", nullable = false)
    private BigCategory bigCategory;
}
