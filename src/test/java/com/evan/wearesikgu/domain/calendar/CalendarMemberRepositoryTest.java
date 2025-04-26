package com.evan.wearesikgu.domain.calendar;

import com.evan.wearesikgu.domain.calendar.dtos.CalendarMemberDTO;
import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.calendar.entity.CalendarMember;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarFoodRepository;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarMemberRepository;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CalendarMemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarMemberRepository calendarMemberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @DisplayName("캘린더에 멤버가 초대 되면 캘린더에 초대된 멤버의 수가 1늘어난다.")
    public void 캘린더에_멤버_초대하기() throws Exception {
        //given
        Member ownerMember = memberRepository.findByEmail("test1@gmail.com");
        Member invitedMember = memberRepository.findByEmail("test2@gmail.com");

        Calendar calendar = calendarRepository.findByOwner(ownerMember);

        List<CalendarMember> beforeCalendarMembers = calendarMemberRepository.findByCalendar(calendar);
        //when
        CalendarMemberDTO calendarMemberDTO = CalendarMemberDTO.builder()
                .participant(invitedMember)
                .calendar(calendar)
                .build();
        CalendarMember calendarMember = modelMapper.map(calendarMemberDTO, CalendarMember.class);
        calendarMemberRepository.save(calendarMember);
        List<CalendarMember> afterCalendarMembers = calendarMemberRepository.findByCalendar(calendar);

        //then
        int size = afterCalendarMembers.size();
        assertThat(size).isEqualTo(beforeCalendarMembers.size() + 1);
//        assertThat(afterCalendarMembers.get(size - 1).getParticipant().getName()).isEqualTo(invitedMember.getName());
    }

    @Test
    @DisplayName("캘린더에 멤버가 초대된 멤버 이름 확인하기")
    @Transactional
    public void 캘린더에_초대된_멤버_이름_확인하기() throws Exception {
        //given
        Member ownerMember = memberRepository.findByEmail("test1@gmail.com");
        Member invitedMember = memberRepository.findByEmail("test2@gmail.com");

        Calendar calendar = calendarRepository.findByOwner(ownerMember);

        //when
        CalendarMemberDTO calendarMemberDTO = CalendarMemberDTO.builder()
                .participant(invitedMember)
                .calendar(calendar)
                .build();
        CalendarMember calendarMember = modelMapper.map(calendarMemberDTO, CalendarMember.class);
        calendarMemberRepository.save(calendarMember);
        List<CalendarMember> afterCalendarMembers = calendarMemberRepository.findByCalendar(calendar);

        //then
        int size = afterCalendarMembers.size();
        assertThat(afterCalendarMembers.get(size - 1).getParticipant().getName()).isEqualTo(invitedMember.getName());
    }
}
