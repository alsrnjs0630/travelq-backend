package com.travelq.backend.security.dto;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();
    Map<String, Object> getAttributes();
}
