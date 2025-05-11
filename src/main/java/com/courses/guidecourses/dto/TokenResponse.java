package com.courses.guidecourses.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponse {
    String access_token;
    String refresh_token;
    long   expires_in;
    String token_type;
    String scope;
}