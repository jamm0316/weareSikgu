package com.evan.wearesikgu.domain.history.dtos;

import com.evan.wearesikgu.domain.history.entity.History;
import com.evan.wearesikgu.domain.history.entity.Reply;
import com.evan.wearesikgu.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO {
    private Long id;
    private String comment;
    private Member writer;
    private History history;
    private Reply parentReply;
}
