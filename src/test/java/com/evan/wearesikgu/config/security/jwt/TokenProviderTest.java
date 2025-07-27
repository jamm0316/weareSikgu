package com.evan.wearesikgu.config.security.jwt;

import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import com.evan.wearesikgu.domain.member.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("generateToken(): 유저 정보를 전달해 토큰을 만들 수 있다.")
    public void generateToken() throws Exception {
        //given
        String userId = "23";

        //when
        String token = jwtProvider.generateToken(userId);

        //then
        String tokenUserId = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);

        assertThat(userId).isEqualTo(tokenUserId);
    }

    @Test
    @DisplayName("validToken(): 만료된 토큰인 때에 예외를 던진다.")
    public void validToken_invalidToken() throws Exception {
        //given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when
        BaseException result = null;
        try {
            jwtProvider.validToken(token);
        } catch (BaseException e) {
            result = e;
        }

        //then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BaseResponseStatus.TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("validToken(): 유효한 토큰인 때에는 유효성 검증에 성공한다.")
    public void validToken_validToken() throws Exception {
        //given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        //when
        boolean result = jwtProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }
}
