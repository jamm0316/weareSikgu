package com.evan.wearesikgu.auth.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class OAuthUserInfo {
    protected String providerId;
    protected String name;
    protected String email;
    protected String profileImageUrl;

    public OAuthUserInfo(String providerId, String name, String email, String profileImageUrl) {
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public abstract OAuth2Provider getProvider();
}
