package com.evan.wearesikgu.domain.calendar.dtos;

import com.evan.wearesikgu.common.enums.MealTime;
import com.evan.wearesikgu.common.enums.Status;
import com.evan.wearesikgu.common.superentity.SuperEntity;
import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.food.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarFoodDTO {
    private Long id;
    private LocalDate mealDate;
    private MealTime mealTime;
    private Status status;
    private Timestamp suggestAt;
    private Food food;
    private Calendar calendar;
}
