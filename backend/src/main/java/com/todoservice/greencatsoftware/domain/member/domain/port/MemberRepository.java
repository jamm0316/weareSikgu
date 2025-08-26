package com.todoservice.greencatsoftware.domain.member.domain.port;

import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> getMemberByIdOrThrow(Long id);
    Member save(Member member);
}
