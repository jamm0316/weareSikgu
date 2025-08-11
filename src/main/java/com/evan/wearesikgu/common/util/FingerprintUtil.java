package com.evan.wearesikgu.common.util;

import jakarta.servlet.http.HttpServletRequest;

public final class FingerprintUtil {
    public static String extractClientIp(HttpServletRequest request) {
        String[] headers = {"X=Forwarded-For", "X-Real-IP", "CF-Connecting-IP"};
        for (String h : headers) {
            String v = request.getHeader(h);
            if (v != null && !v.isBlank()) return v.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public static String ipPrefix(String ip) {
        if (ip.contains(":")) return ip.split(":")[0] + ":" + ip.split(":")[1];  //IPv6 → 64 정도
        return ip.substring(0, ip.lastIndexOf(":"));
    }
}
