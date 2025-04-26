package com.evan.wearesikgu.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("회원 가입이 제대로 실행되면 해당 아이디에 비밀번호가 저장한 값과 일치해야한다.")
    public void 회원가입 () throws Exception {
        //given
        SignupDTO build = SignupDTO.builder()
                .email("test2@gmail.com")
                .password("12341234")
                .profileImageUrl("default.png")
                .name("sofia")
                .nickName("dangdang")
                .phoneNumber("010-3315-2157")
                .build();
        Member entity = modelMapper.map(build, Member.class);

//        SignupDTO build = SignupDTO.builder()
//                .email("test1@gmail.com")
//                .password("1234")
//                .profileImageUrl("default.png")
//                .name("evan")
//                .nickName("jamm")
//                .phoneNumber("010-6682-6308")
//                .build();
//        Member entity = modelMapper.map(build, Member.class);

        //when
        memberRepository.save(entity);

        //then
        Member findMember = memberRepository.findByEmail("test2@gmail.com");
        assertThat(findMember.getPassword()).isEqualTo("12341234");
        assertThat(findMember.getName()).isEqualTo("sofia");
        assertThat(findMember.getNickName()).isEqualTo("dangdang");

//        Member findMember = memberRepository.findByEmail("test1@gmail.com");
//        assertThat(findMember.getPassword()).isEqualTo("1234");
//        assertThat(findMember.getName()).isEqualTo("evan");
//        assertThat(findMember.getNickName()).isEqualTo("jamm");
    }
}
