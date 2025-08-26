package com.todoservice.greencatsoftware.domain.member.infrastructure.persistence;

import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.domain.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SpringDataMemberJpaRepository extends JpaRepository<Member, Long> {

    @Repository
    @RequiredArgsConstructor
    class MemberJpaRepositoryImpl implements MemberRepository {
        private final SpringDataMemberJpaRepository jpa;

        @Override
        public Optional<Member> getMemberByIdOrThrow(Long id) {
            return jpa.findById(id);
        }

        @Override
        public Member save(Member member) {
            return jpa.save(member);
        }
    }
}
