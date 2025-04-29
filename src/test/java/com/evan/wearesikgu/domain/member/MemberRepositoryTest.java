package com.evan.wearesikgu.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private SignupRequestDTO build = SignupRequestDTO.builder()
            .email("test6@gmail.com")
            .password("12341234")
            .profileImageUrl("default.png")
            .name("sofia")
            .nickName("dangdang")
            .phoneNumber("010-3315-0987")
            .build();

    @Test
    @DisplayName("회원 가입이 제대로 실행되면 해당 아이디에 비밀번호가 저장한 값과 일치해야한다.")
    public void 회원가입 () throws Exception {
        //given
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
        Member findMember = memberRepository.findByEmail("test6@gmail.com");

        //then
        assertThat(findMember.getPassword()).isEqualTo("12341234");
        assertThat(findMember.getName()).isEqualTo("sofia");
        assertThat(findMember.getNickName()).isEqualTo("dangdang");
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-3315-0987");

//        Member findMember = memberRepository.findByEmail("test1@gmail.com");
//        assertThat(findMember.getPassword()).isEqualTo("1234");
//        assertThat(findMember.getName()).isEqualTo("evan");
//        assertThat(findMember.getNickName()).isEqualTo("jamm");
    }
}
