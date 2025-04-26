package com.evan.wearesikgu.domain.history.model.repository;

import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.history.entity.History;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByCalendarFood(@NotNull(message = "캘린더음식은 필수입니다.") CalendarFood calendarFood);
}
