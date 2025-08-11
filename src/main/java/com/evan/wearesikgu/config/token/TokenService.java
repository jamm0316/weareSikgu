package com.evan.wearesikgu.config.token;

import com.evan.wearesikgu.common.baseResponse.BaseResponseStatus;
import com.evan.wearesikgu.common.exception.BaseException;
import com.evan.wearesikgu.common.util.FingerprintUtil;
import com.evan.wearesikgu.config.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
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

    public TokenResponse generateTokenPair(String userId, String uaHash, String ipPrefix) {
        String accessToken = jwtProvider.generateToken(userId);
        String deviceId = FingerprintUtil.deviceId(uaHash, ipPrefix);

        String k = "rt:" + userId + ":" + deviceId;
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForHash().putAll(k, Map.of(
                "refreshToken", refreshToken,
                "uaHash", uaHash,
                "ipPrefix", ipPrefix,
                "createdAt", String.valueOf(System.currentTimeMillis()),
                "lastSean", String.valueOf(System.currentTimeMillis())
        ));

        redisTemplate.expire(k, Duration.ofDays(7));
        return new TokenResponse(accessToken, refreshToken);
    }

    public String reissueAccessToken(String accessToken, String refreshToken, String uaHashNow, String ipPrefixNow) {
        String userIdFromToken = jwtProvider.getUserIdFromToken(accessToken);
        String deviceId = FingerprintUtil.deviceId(uaHashNow, ipPrefixNow);
        String k = "rt:" + userIdFromToken + ":" + deviceId;

        Map<Object, Object> stored = redisTemplate.opsForHash().entries(k);

        if (!refreshToken.equals(stored.get("refreshToken")) ||
                !uaHashNow.equals(stored.get("uaHash")) ||
                !ipPrefixNow.equals(stored.get("ipPrefix"))) {
            log.warn("RefreshToken mismatch: userId={}, diviceId={}", userIdFromToken, deviceId);

            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        String newRefreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(k, "refreshToken", newRefreshToken);
        redisTemplate.opsForHash().put(k, "lastSean", String.valueOf(System.currentTimeMillis()));

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