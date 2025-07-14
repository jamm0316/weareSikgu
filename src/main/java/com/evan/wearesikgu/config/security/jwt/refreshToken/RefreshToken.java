package com.evan.wearesikgu.config.security.jwt.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private String userId;
    private String refreshToken;
}
