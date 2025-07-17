package com.evan.wearesikgu.domain.member;

import com.evan.wearesikgu.auth.oauth.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String mail);

    boolean existsByEmail(String email);

    Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String string);
}
