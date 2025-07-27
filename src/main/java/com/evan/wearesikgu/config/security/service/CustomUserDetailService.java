package com.evan.wearesikgu.config.security.service;

import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findById(Long.parseLong(userId))
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword() != null ? member.getPassword() : "")  //소셜로그인 유저면 빈 비밀번호 가능
                .authorities("ROLE_USER")  //기본 권한 부여
                .build();
    }
}
