package com.travelq.backend.security.dto;

import java.util.Map;

public class GoogleUserInfoDTO implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public GoogleUserInfoDTO(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "Google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
