package com.evan.wearesikgu.domain.calendar.dtos;

import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarMemberDTO {
    private Long id;
    private Member participant;
    private Calendar calendar;
}
