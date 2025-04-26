package com.evan.wearesikgu.domain.history.model.repository;

import com.evan.wearesikgu.domain.history.entity.EmojiReaction;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmojiReactionRepository extends JpaRepository<EmojiReaction, Long> {
    List<EmojiReaction> findByEmoji(@NotNull(message = "이모지는 필수입니다.") String emoji);
}
