package com.todoservice.greencatsoftware.domain.member.application;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.domain.port.MemberRepository;
import com.todoservice.greencatsoftware.domain.member.presentation.dto.MemberCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;

    public Member createMember(MemberCreateRequest request) {
        return memberRepository.save(memberFactory.createMember(request));
    }

    public Member getMemberByIdOrThrow(Long id) {
        return memberRepository.getMemberByIdOrThrow(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MISSING_START_TIME_VALUE));
    }
}
