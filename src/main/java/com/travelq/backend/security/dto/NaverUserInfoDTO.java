package com.travelq.backend.security.dto;

import java.util.Map;

public class NaverUserInfoDTO implements OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final Map<String, Object> response;

    public NaverUserInfoDTO(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "Naver";
    }

    @Override
    public String getProviderId() {
        return response.get("id").toString();
    }

    @Override
    public String getEmail() {
        return response.get("email").toString();
    }

    @Override
    public String getName() {
        return response.get("name").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }
}
