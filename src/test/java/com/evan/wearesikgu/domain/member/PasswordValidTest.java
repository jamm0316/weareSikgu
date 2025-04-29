package com.evan.wearesikgu.domain.member;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordValidTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 이메일형식이_잘못되었으면_에러발생() {
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("wrong-email")
                .password("test1234!")
                .build();

        Set<ConstraintViolation<SignupRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(
                violation -> violation.getPropertyPath().toString().equals("email")
        );
    }

    @Test
    void 비밀번호형식이_잘못되었으면_에러발생() {
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("test@gmail.com")
                .password("simple")
                .build();

        Set<ConstraintViolation<SignupRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(
                violation -> violation.getPropertyPath().toString().equals("password")
        );
    }
}
