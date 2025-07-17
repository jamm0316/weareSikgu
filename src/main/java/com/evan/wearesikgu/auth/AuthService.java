package com.evan.wearesikgu.auth;

import com.evan.wearesikgu.auth.oauth.OAuth2Provider;
import com.evan.wearesikgu.auth.oauth.OAuthServiceFactory;
import com.evan.wearesikgu.auth.oauth.OAuthUserInfo;
import com.evan.wearesikgu.config.security.jwt.TokenProvider;
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
    final private TokenProvider tokenProvider;

    @Transactional
    public String login (OAuthUserInfo userInfo) {
        Member member = memberRepository.findByProviderAndProviderId(
                userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(() -> signIn(userInfo));

        return tokenProvider.generateToken(member.getId().toString());
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
