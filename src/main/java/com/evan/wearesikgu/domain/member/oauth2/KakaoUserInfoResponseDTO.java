package com.evan.wearesikgu.domain.member.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponseDTO {
    private Long id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        @JsonProperty("profile")
        private Profile profile;
    }

    @Getter
    @NoArgsConstructor
    public static class Profile {
        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
