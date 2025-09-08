package com.todoservice.greencatsoftware.common.util;

import com.todoservice.greencatsoftware.config.security.jwt.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final int ACCESS_TOKEN_MAX_AGE_IN_SECONDS = 60 * 15;  //15분.
    private static final int REFRESH_TOKEN_MAX_AGE_IN_SECONDS = 60 * 60 * 24 * 7;  //7일


    public static Cookie createCookies(String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public static void addTokenCookies(HttpServletResponse response, TokenResponse tokenResponse) {
        Cookie accessToken =
                createCookies("access_token", tokenResponse.getAccessToken(), ACCESS_TOKEN_MAX_AGE_IN_SECONDS);
        Cookie refreshToken =
                createCookies("refresh_token", tokenResponse.getRefreshToken(), REFRESH_TOKEN_MAX_AGE_IN_SECONDS);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    public static void deleteCookie(HttpServletResponse response, String tokenName) {
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
}
