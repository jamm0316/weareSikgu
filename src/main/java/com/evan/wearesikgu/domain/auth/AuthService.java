package com.evan.wearesikgu.domain.auth;

import com.evan.wearesikgu.common.baseResponse.BaseResponse;
import com.evan.wearesikgu.config.security.jwt.TokenProvider;
import com.evan.wearesikgu.domain.auth.kakao.KaKaoOAuthService;
import com.evan.wearesikgu.domain.auth.kakao.KakaoUserInfoResponseDTO;
import com.evan.wearesikgu.domain.member.Member;
import com.evan.wearesikgu.domain.member.MemberRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private KaKaoOAuthService kaKaoOAuthService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Transactional
    public BaseResponse<String> kakaoLogin(String code) {
        //1. 카카오로부터 Access Token 및 사용자 정보 받아오기
        String oauthAccessToken = kaKaoOAuthService.getAccessToken(code);
        KakaoUserInfoResponseDTO userInfo = kaKaoOAuthService.getUserInfo(oauthAccessToken);

        //2. 카카오 이메일로 DB에서 회원 조회
        long userId = userInfo.getId();
        Member member = memberRepository.findById(userId)
                .orElseGet(() -> {
                    // 회원가입 처리
                    Member newMember = new Member();
                    newMember.setEmail("test@example.com");
                    newMember.setName(userInfo.getKakaoAccount().getProfile().getNickname());
                    newMember.setNickName(userInfo.getKakaoAccount().getProfile().getNickname());
                    newMember.setProfileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
                    newMember.setPassword("social_login_user");
                    return memberRepository.save(newMember); // 신규 회원 저장
                });

        String apiAccessToken = tokenProvider.generateToken(member.getId().toString());
        return new BaseResponse<>(apiAccessToken);
    }
}
