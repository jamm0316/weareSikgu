package com.todoservice.greencatsoftware.domain.member;

import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.infrastructure.persistence.SpringDataMemberJpaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SpringDataMemberRepositoryTest {
    @Autowired
    SpringDataMemberJpaRepository memberRepository;

    @Test
    @DisplayName("insert 성공")
    public void persist_ok_all_constraints() throws Exception {
        //given
        Member member = Member.create("test@email.com", "Test1234@!#$", null, "testName");

        //when
        Member saved = memberRepository.saveAndFlush(member);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("email 형식이 아니면 persist 시점에 ConstraintViolationException")
    public void persist_fail_invalid_email() throws Exception {
        //given
        Member member = Member.create("asd", "Test1234@!#$", null, "testName");

        //then
        assertThatThrownBy(() -> memberRepository.saveAndFlush(member))
        .isInstanceOf(ConstraintViolationException.class)
        .hasMessageContaining("이메일 형식이 올바르지 않습니다.");
    }
    
    @Test
    @DisplayName("password 패턴 위반이면 persist 시점에 ConstraintViolationException")
    public void persist_fail_invalid_password() throws Exception {
        //given
        Member member = Member.create("email@email.com", "123456789", null, "testName");

        //then
        assertThatThrownBy(() -> memberRepository.saveAndFlush(member))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
    }

    @Test
    @DisplayName("password 길이가 8자 이하면 persist 시점에 ConstraintViolationException")
    public void persist_fail_invalid_password_size() throws Exception {
        //given
        Member member = Member.create("email@email.com", "1234", null, "testName");

        //then
        assertThatThrownBy(() -> memberRepository.saveAndFlush(member))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("비밀번호는 8자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("email UNIQUE 위반 시 DataIntegrityViolationException")
    public void persist_fail_unique_constraint() throws Exception {
        //given
        Member member1 = Member.create("email@email.com", "Test1234@!#$", null, "testName1");
        Member member2 = Member.create("email@email.com", "1234Test@!#$", null, "testName2");

        //when
        memberRepository.saveAndFlush(member1);

        //then
        assertThatThrownBy(() -> memberRepository.saveAndFlush(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
