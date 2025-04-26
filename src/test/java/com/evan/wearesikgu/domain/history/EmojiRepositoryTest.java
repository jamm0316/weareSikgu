package com.evan.wearesikgu.domain.history;

import com.evan.wearesikgu.domain.history.dtos.EmojiReactionDTO;
import com.evan.wearesikgu.domain.history.entity.EmojiReaction;
import com.evan.wearesikgu.domain.history.entity.History;
import com.evan.wearesikgu.domain.history.entity.Reply;
import com.evan.wearesikgu.domain.history.model.repository.EmojiReactionRepository;
import com.evan.wearesikgu.domain.history.model.repository.HistoryRepository;
import com.evan.wearesikgu.domain.history.model.repository.ReplyRepository;
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
public class EmojiRepositoryTest {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    EmojiReactionRepository emojiReactionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @DisplayName("히스토리에 이모지 등록하면 이모지리액션에 저장된다.")
    @Transactional
    public void 히스토리에_이모지등록() throws Exception {
        //given
        String emoji = "❤️";
        History history = historyRepository.findById(2L).orElseThrow();
        Member member = memberRepository.findById(2L).orElseThrow();

        //when
        EmojiReactionDTO emojiReactionDTO = EmojiReactionDTO.builder()
                .emoji(emoji)
                .history(history)
                .reactor(member)
                .build();

        EmojiReaction emojiReaction = modelMapper.map(emojiReactionDTO, EmojiReaction.class);

        EmojiReaction savedEmojiReaction = emojiReactionRepository.save(emojiReaction);

        //then
        assertThat(savedEmojiReaction.getEmoji()).isEqualTo("❤️");
        assertThat(savedEmojiReaction.getHistory().getId()).isEqualTo(history.getId());
        assertThat(savedEmojiReaction.getReply()).isNull();
    }

    @Test
    @DisplayName("댓글에 이모지 등록하면 이모지리액션에 저장된다.")
    @Transactional
    public void 댓글에_이모지등록() throws Exception {
        //given
        String emoji = "❤️";
        Reply reply = replyRepository.findById(1L).orElseThrow();
        Member member = memberRepository.findById(2L).orElseThrow();

        //when
        EmojiReactionDTO emojiReactionDTO = EmojiReactionDTO.builder()
                .emoji(emoji)
                .reply(reply)
                .reactor(member)
                .build();

        EmojiReaction emojiReaction = modelMapper.map(emojiReactionDTO, EmojiReaction.class);

        EmojiReaction savedEmojiReaction = emojiReactionRepository.save(emojiReaction);

        //then
        assertThat(savedEmojiReaction.getEmoji()).isEqualTo("❤️");
        assertThat(savedEmojiReaction.getHistory()).isNull();
        assertThat(savedEmojiReaction.getReply().getId()).isEqualTo(reply.getId());
        assertThat(savedEmojiReaction.getReply().getParentReply()).isNull();
    }

    @Test
    @DisplayName("대댓글에 이모지 등록하면 이모지리액션에 저장된다.")
    @Transactional
    public void 대댓글에_이모지_등록() throws Exception {
        //given
        String emoji = "❤️";
        Reply reReply = replyRepository.findById(2L).orElseThrow();
        Member member = memberRepository.findById(2L).orElseThrow();

        //when
        EmojiReactionDTO emojiReactionDTO = EmojiReactionDTO.builder()
                .emoji(emoji)
                .reply(reReply)
                .reactor(member)
                .build();

        EmojiReaction emojiReaction = modelMapper.map(emojiReactionDTO, EmojiReaction.class);

        EmojiReaction savedEmojiReaction = emojiReactionRepository.save(emojiReaction);

        //then
        assertThat(savedEmojiReaction.getEmoji()).isEqualTo("❤️");
        assertThat(savedEmojiReaction.getHistory()).isNull();
        assertThat(savedEmojiReaction.getReply().getId()).isEqualTo(reReply.getId());
        assertThat(savedEmojiReaction.getReply().getParentReply().getId()).isEqualTo(reReply.getParentReply().getId());

    }
}
