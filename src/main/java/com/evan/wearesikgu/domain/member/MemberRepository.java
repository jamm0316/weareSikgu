package com.evan.wearesikgu.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String mail);

    boolean existsByEmail(String email);
}
