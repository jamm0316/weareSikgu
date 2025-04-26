package com.evan.wearesikgu.domain.history;

import com.evan.wearesikgu.domain.calendar.entity.CalendarFood;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarFoodRepository;
import com.evan.wearesikgu.domain.calendar.model.repository.CalendarRepository;
import com.evan.wearesikgu.domain.history.dtos.ReplyDTO;
import com.evan.wearesikgu.domain.history.entity.History;
import com.evan.wearesikgu.domain.history.entity.Reply;
import com.evan.wearesikgu.domain.history.model.repository.HistoryRepository;
import com.evan.wearesikgu.domain.history.model.repository.ReplyRepository;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ReplyRepositoryTest {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    CalendarFoodRepository calendarFoodRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Test
    @DisplayName("댓글을 등록하면 reply에 저장된다.")
    public void 댓글등록() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test2@gmail.com");
        CalendarFood calendarFood = calendarFoodRepository.findById(1L).orElseThrow();
        History history = historyRepository.findById(2L).orElseThrow();
        String comment = "다음에 또해죠~!!";

        //when
        ReplyDTO replyDTO = ReplyDTO.builder()
                .writer(member)
                .comment(comment)
                .history(history)
                .build();

        Reply reply = modelMapper.map(replyDTO, Reply.class);

        Reply savedReply = replyRepository.save(reply);

        //then
        assertThat(savedReply.getComment()).isEqualTo("다음에 또해죠~!!");
    }

    @Test
    @DisplayName("대댓글을 등록하면 parent_reply아래 등록된다.")
    public void 대댓글_등록() throws Exception {
        //given
        Member member = memberRepository.findByEmail("test1@gmail.com");
        CalendarFood calendarFood = calendarFoodRepository.findById(1L).orElseThrow();
        History history = historyRepository.findById(2L).orElseThrow();
        String comment = "알겠어 또해줄게~";
        Reply parentreply = replyRepository.findById(1L).orElseThrow();

        //when
        ReplyDTO replyDTO = ReplyDTO.builder()
                .writer(member)
                .comment(comment)
                .history(history)
                .parentReply(parentreply)
                .build();

        Reply reply = modelMapper.map(replyDTO, Reply.class);

        Reply savedReply = replyRepository.save(reply);

        //then
        assertThat(savedReply.getComment()).isEqualTo("알겠어 또해줄게~");
        assertThat(savedReply.getParentReply().getId()).isEqualTo(1L);
    }
}
