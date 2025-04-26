package com.evan.wearesikgu.domain.history.entity;

import com.evan.wearesikgu.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "댓글은 필수입니다.")
    @Column(nullable = false)
    private String comment;

    @NotNull(message = "작성자는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @NotNull(message = "댓글을 남길 게시글은 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    private History history;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reply parentReply;
}
