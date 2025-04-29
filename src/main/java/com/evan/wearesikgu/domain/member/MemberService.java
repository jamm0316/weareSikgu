package com.evan.wearesikgu.domain.member;

import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ModelMapper modelMapper;

    public SignupResponseVO save(SignupRequestDTO signupDTO) {
        if (memberRepository.existsByEmail(signupDTO.getEmail())) throw new BaseException(BaseResponseStatus.DUPLICATE_MEMBER);

        Member entity = modelMapper.map(signupDTO, Member.class);
        Member savedEntity = memberRepository.save(entity);

        return SignupResponseVO.builder()
                .email(savedEntity.getEmail())
                .name(savedEntity.getName())
                .nickName(savedEntity.getNickName())
                .profileImageUrl(savedEntity.getProfileImageUrl())
                .build();
    }
}
