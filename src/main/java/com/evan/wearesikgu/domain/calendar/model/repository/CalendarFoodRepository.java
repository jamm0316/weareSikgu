package com.evan.wearesikgu.domain.calendar.model.repository;

import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarFoodRepository extends JpaRepository<CalendarFood, Long> {
    List<CalendarFood> findAllByCalendar(@NotNull(message = "캘린더는 필수입니다.") Calendar calendar);
}
