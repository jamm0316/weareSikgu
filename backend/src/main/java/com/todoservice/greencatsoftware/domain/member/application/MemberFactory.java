package com.todoservice.greencatsoftware.domain.member.application;

import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.presentation.dto.MemberCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberFactory {
    public Member createMember(MemberCreateRequest request) {
        return Member.create(request.email(), request.password(), request.profileImageUrl(), request.name());
    }
}
