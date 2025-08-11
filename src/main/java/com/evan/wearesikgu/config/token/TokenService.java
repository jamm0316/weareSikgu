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

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenService {

    private final JWTProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final String PREFIX = "token:";
    private final String RT_PREFIX = "rt";
    private final String H_REFRESH_TOKEN = "refreshToken";
    private final String H_UA_HASH = "uaHash";
    private final String H_IP_PREFIX = "ipPrefix";
    private final String H_LAST_SEEN = "lastSeen";
    private final String H_CREAT_AT = "lastSeen";

    public TokenResponse generateTokenPair(String userId, String uaHash, String ipPrefix) {
        String accessToken = jwtProvider.generateToken(userId);
        String deviceId = FingerprintUtil.deviceId(uaHash, ipPrefix);

        String k = RT_PREFIX + ":" + userId + ":" + deviceId;
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForHash().putAll(k, Map.of(
                H_REFRESH_TOKEN, refreshToken,
                H_UA_HASH, uaHash,
                H_IP_PREFIX, ipPrefix,
                H_CREAT_AT, String.valueOf(System.currentTimeMillis()),
                H_LAST_SEEN, String.valueOf(System.currentTimeMillis())
        ));

        redisTemplate.expire(k, Duration.ofDays(7));
        return new TokenResponse(accessToken, refreshToken);
    }

    public String reissueAccessToken(String accessToken, String refreshToken, String uaHashNow, String ipPrefixNow) {
        String userIdFromToken = jwtProvider.getUserIdFromToken(accessToken);
        String deviceId = FingerprintUtil.deviceId(uaHashNow, ipPrefixNow);
        String k = RT_PREFIX + ":" + userIdFromToken + ":" + deviceId;

        Map<Object, Object> stored = redisTemplate.opsForHash().entries(k);

        if (!refreshToken.equals(stored.get(H_REFRESH_TOKEN)) ||
                !uaHashNow.equals(stored.get(H_UA_HASH)) ||
                !ipPrefixNow.equals(stored.get(H_IP_PREFIX))) {
            log.warn("RefreshToken mismatch: userId={}, deviceId={}", userIdFromToken, deviceId);

            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        String newRefreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(k, H_REFRESH_TOKEN, newRefreshToken);
        redisTemplate.opsForHash().put(k, H_LAST_SEEN, String.valueOf(System.currentTimeMillis()));

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