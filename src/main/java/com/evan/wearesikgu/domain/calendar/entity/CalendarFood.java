package com.evan.wearesikgu.domain.calendar.entity;

import com.evan.wearesikgu.common.enums.MealTime;
import com.evan.wearesikgu.common.enums.Status;
import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.food.entity.Food;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class CalendarFood extends SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "음식 날짜는 필수입니다.")
    @Column(nullable = false)
    private LocalDate mealDate;

    @NotNull(message = "음식 시간은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealTime mealTime;

    @NotNull(message = "suggest가 defualt입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @NotNull(message = "제안된 시간은 생성된 시간이 default 입니다.")
    @Column(nullable = false)
    private Timestamp suggestAt;

    @NotNull(message = "음식은 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @NotNull(message = "캘린더는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;
}
