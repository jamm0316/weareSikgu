package com.evan.wearesikgu.domain.calendar.dtos;

import com.evan.wearesikgu.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDTO {
    private Long id;
    private String name;
    private String inviteToken;
    private Member owner;
}
