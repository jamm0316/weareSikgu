package com.evan.wearesikgu.domain.history.dtos;

import com.evan.wearesikgu.domain.history.entity.History;
import com.evan.wearesikgu.domain.history.entity.Reply;
import com.evan.wearesikgu.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmojiReactionDTO {
    private Long id;
    private String emoji;
    private Member reactor;
    private Reply reply;
    private History history;
}
