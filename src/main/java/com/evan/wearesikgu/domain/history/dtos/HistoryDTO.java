package com.evan.wearesikgu.domain.history.dtos;

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
public class HistoryDTO {
    private Long id;
    private String imageUrl;
    private String comment;
    private CalendarFood calendarFood;
    private Member member;
}
