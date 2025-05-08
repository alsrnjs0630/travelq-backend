package com.travelq.backend.security.dto;

import java.util.Map;

public class KakaoUserInfoDTO implements OAuth2UserInfo{
    private final Map<String, Object> attributes;
    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccount;

    public KakaoUserInfoDTO(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return properties.get("name").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
