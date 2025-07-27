package com.evan.wearesikgu.auth;

import com.evan.wearesikgu.auth.oauth.OAuthUserInfo;
import com.evan.wearesikgu.config.token.TokenResponse;
import com.evan.wearesikgu.config.token.TokenService;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    final private MemberRepository memberRepository;
    private final TokenService tokenService;

    @Transactional
    public TokenResponse login (OAuthUserInfo userInfo) {
        Member member = memberRepository.findByProviderAndProviderId(
                userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(() -> signIn(userInfo));

        return tokenService.generateTokenPair(member.getId().toString());
    }

    private Member register(OAuthUserInfo userInfo) {
        Member member = memberRepository.findByProviderAndProviderId(
                        userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(() -> signIn(userInfo));

        return memberRepository.save(member);
    }

    public Member signIn(OAuthUserInfo userInfo) {
        String randomEmail = UUID.randomUUID().toString();

        Member newMember = new Member();
        newMember.setEmail(randomEmail + "@email.com");
        newMember.setProviderId(userInfo.getProviderId());
        newMember.setProvider(userInfo.getProvider());
        newMember.setNickName(userInfo.getName());
        newMember.setName(userInfo.getName());
        newMember.setProfileImageUrl(userInfo.getProfileImageUrl());
        newMember.setPassword("{noop}social_login_user");

        return memberRepository.save(newMember);
    }
}
