package com.evan.wearesikgu.domain.calendar;

import com.evan.wearesikgu.common.enums.MealTime;
import com.evan.wearesikgu.common.enums.Status;
import com.evan.wearesikgu.domain.calendar.dtos.CalendarFoodDTO;
import com.evan.wearesikgu.domain.calendar.entity.Calendar;
import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarFoodRepository;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarRepository;
import com.evan.wearesikgu.domain.food.entity.Food;
import com.evan.wearesikgu.domain.food.entity.FoodRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CalendarFoodRepositoryTest {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarFoodRepository calendarFoodRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Transactional
    public void 캘린더음식이_생성되는지_확인() throws Exception {
        //given
        Food food = foodRepository.findById(1L).orElseThrow();
        Member owner = memberRepository.findById(1L).orElseThrow();
        Calendar calendar = calendarRepository.findByOwner(owner);

        CalendarFoodDTO calendarFoodDTO = CalendarFoodDTO.builder()
                .mealTime(MealTime.BREAKFAST)
                .status(Status.SUGGEST)
                .suggestAt(Timestamp.valueOf(LocalDateTime.now()))
                .mealDate(LocalDate.now())
                .calendar(calendar)
                .food(food)
                .build();
        //when
        CalendarFood calendarFood = modelMapper.map(calendarFoodDTO, CalendarFood.class);
        CalendarFood savedCalendarFood = calendarFoodRepository.save(calendarFood);

        //then
        assertThat(savedCalendarFood.getStatus()).isEqualTo(Status.SUGGEST);
        assertThat(savedCalendarFood.getCalendar().getOwner().getEmail()).isEqualTo(owner.getEmail());
        assertThat(savedCalendarFood.getFood().getBigCategory().getName()).isEqualTo("한식");
    }

    @Test
    @Transactional
    public void 캘린더로_캘린더_음식_조회() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test1@gmail.com");
        Calendar calendar = calendarRepository.findByOwner(member);

        //when
        List<CalendarFood> calendarFood = calendarFoodRepository.findAllByCalendar(calendar);

        //then
        assertThat(calendarFood.get(0).getFood().getId()).isEqualTo(1L);
        assertThat(calendarFood.get(0).getFood().getName()).isEqualTo("돼지불백");
    }
}
