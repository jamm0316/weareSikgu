package com.evan.wearesikgu.config.token;

import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import com.evan.wearesikgu.config.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenService {

    private final JWTProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final String prefix = "token:";

    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60;  //7일 (초 단위)

    public TokenResponse generateTokenPair(String userId) {
        String accessToken = jwtProvider.generateToken(userId);

        String refreshToken = redisTemplate.opsForValue().get(userId);
        if (refreshToken == null) {
            refreshToken = UUID.randomUUID().toString();
            String redisValue = prefix + refreshToken;
            redisTemplate.opsForValue().set(userId, redisValue, REFRESH_TOKEN_EXPIRY, TimeUnit.SECONDS);
        }

        refreshToken = refreshToken.substring(prefix.length());

        return new TokenResponse(accessToken, refreshToken);
    }

    public String reissueAccessToken(String accessToken, String refreshToken) {
        String userIdFromToken = jwtProvider.getUserIdFromToken(accessToken);
        String refreshTokenByUserId = redisTemplate.opsForValue().get(userIdFromToken).substring(prefix.length());

        if (refreshTokenByUserId == null || !refreshTokenByUserId.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        return jwtProvider.generateToken(userIdFromToken);
    }

    public void deleteRefreshToken(String accessToken, String refreshToken) {
        String userIdFromToken = jwtProvider.getUserIdFromToken(accessToken);
        String refreshTokenByUserId = redisTemplate.opsForValue().get(userIdFromToken);

        if (refreshTokenByUserId != null && refreshTokenByUserId.equals(refreshToken)) {
            redisTemplate.delete(userIdFromToken);
        } else {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }
    }
}