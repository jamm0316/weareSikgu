package com.evan.wearesikgu.domain.calendar.dtos;

import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationDTO {
    private Long id;
    private CalendarFood calendarFood;
    private Member member;
}
