package com.evan.wearesikgu.domain.calendar;

import com.evan.wearesikgu.domain.calendar.dtos.ParticipationDTO;
import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.calendar.entity.Participation;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarFoodRepository;
import com.evan.wearesikgu.domain.calendar.model.repository.ParticipationRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParticipationRepositoryTest {
    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private CalendarFoodRepository calendarFoodRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("내가 초대된 calendar에 올라온 CalendarFood를 조회하고, '먹을래요'버튼을 누르면 저장된다.")
    @Transactional
    public void 먹을래요_버튼_클릭() throws Exception {
        //given
        Member invitedMember = memberRepository.findByEmail("test1@gmail.com");
        CalendarFood calendarFood = calendarFoodRepository.findById(2L).orElseThrow();

        //when
        ParticipationDTO participationDTO = ParticipationDTO.builder()
                .calendarFood(calendarFood)
                .member(invitedMember)
                .build();
        Participation participation = modelMapper.map(participationDTO, Participation.class);
        Participation savedParticipation = participationRepository.save(participation);

        //then
        assertThat(savedParticipation.getMember().getName()).isEqualTo(invitedMember.getName());
    }
}
