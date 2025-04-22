package com.evan.wearesikgu.domain.calendar;

import com.evan.wearesikgu.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Calendar {
    @Id
    private Long id;

    String name;

    @Column(length = 64)
    String inviteToken;

    @ManyToOne
    Member member;
}
