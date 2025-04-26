package com.evan.wearesikgu.domain.calendar;

import com.evan.wearesikgu.domain.calendar.dtos.CalendarDTO;
import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CalendarRepositoryTest {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("캘린더 이름을 입력하면 멤버id 초대링크가 자동으로 생성된다.")
    public void 캘린더생성() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test1@gmail.com");

        CalendarDTO calendarDTO = CalendarDTO.builder()
                .name("evan의 캘린더")
                .owner(member)
                .inviteToken("1234")
                .build();

        Calendar calendarEntity = modelMapper.map(calendarDTO, Calendar.class);

        //when
        Calendar saveCalendar = calendarRepository.save(calendarEntity);

        //then
        assertThat(saveCalendar.getOwner().getEmail()).isEqualTo(member.getEmail());
        assertThat(saveCalendar.getName()).isEqualTo("evan의 캘린더");
    }
}
