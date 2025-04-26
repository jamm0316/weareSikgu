package com.evan.wearesikgu.domain.history.model.repository;

import com.evan.wearesikgu.domain.history.entity.EmojiReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmojiReactionRepository extends JpaRepository<EmojiReaction, Long> {
}
