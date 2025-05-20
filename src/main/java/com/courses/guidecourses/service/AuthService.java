package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.TokenResponse;
import com.courses.guidecourses.dto.UserLoginDto;
import com.courses.guidecourses.exception.AuthException;
import com.courses.guidecourses.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;

    @Value("${app.keycloak.token-url}")
    private String tokenUrl;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public TokenResponse login(UserLoginDto login) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var form = new LinkedMultiValueMap<String,String>();
        form.add("grant_type",    "password");
        form.add("client_id",     clientId);
        form.add("client_secret", clientSecret);
        form.add("username",      login.getUsername());
        form.add("password",      login.getPassword());
        form.add("scope",         "openid");

        var req = new HttpEntity<>(form, headers);

        try {
            var resp = restTemplate.postForEntity(tokenUrl, req, TokenResponse.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new AuthException("AUTH_FAILED", "Login failed: " + resp.getStatusCode());
            }
            return resp.getBody();

        } catch (ResourceAccessException ex) {
            throw new AuthException("AUTH_SERVER_UNAVAILABLE",
                    "Authentication server is unreachable. Please try later.",
                    ex);
        }
    }

    public String extractKeycloakId(TokenResponse token) {
        return JwtUtils.getSubject(token.getAccess_token())
                .orElseThrow(() -> new AuthException(
                        "AUTH_INVALID_TOKEN",
                        "Cannot extract subject (sub) from access token"
                ));
    }

}

