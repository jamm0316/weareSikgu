package com.evan.wearesikgu.domain.calendar.model.repository;

import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.calendar.entity.CalendarMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarMemberRepository extends JpaRepository<CalendarMember, Long> {
    List<CalendarMember> findByCalendar(Calendar calendar);
}
