package com.evan.wearesikgu.domain.history;

import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarFoodRepository;
import com.evan.wearesikgu.domain.history.dtos.HistoryDTO;
import com.evan.wearesikgu.domain.history.entity.History;
import com.evan.wearesikgu.domain.history.model.repository.HistoryRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class HistoryRepositoryTest {
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CalendarFoodRepository calendarFoodRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @DisplayName("캘린더 푸드에 이미지와, 글을 저장할 수 있다.")
    public void 캘린더_푸드에_히스토리_저장하기() throws Exception {
        //given
        Member owner = memberRepository.findByEmail("test1@gmail.com");
        CalendarFood calendarFood = calendarFoodRepository.findById(2L).orElseThrow();
        String imageUrl = "imageSample1.png";

        //when
        HistoryDTO historyDTO = HistoryDTO.builder()
                .imageUrl(imageUrl)
                .comment("너무 맛있었어!!")
                .calendarFood(calendarFood)
                .member(owner)
                .build();
        History history = modelMapper.map(historyDTO, History.class);
        History savedHistory = historyRepository.save(history);

        //then
        assertThat(savedHistory.getComment()).isEqualTo("너무 맛있었어!!");
        assertThat(savedHistory.getImageUrl()).isEqualTo("imageSample1.png");
    }
}
