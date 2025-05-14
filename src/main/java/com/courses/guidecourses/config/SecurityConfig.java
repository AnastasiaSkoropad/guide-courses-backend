package com.courses.guidecourses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // вимикаємо CSRF для простоти (якщо у вас stateless API)
                .csrf(csrf -> csrf.disable())
                // конфігурація авторизації запитів
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI доступ без авторизації
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // Public GET endpoints для курсів, категорій, напрямів, тем і коментарів
                        .requestMatchers(HttpMethod.GET,
                                "/api/courses/**",
                                "/api/categories/**",
                                "/api/categories/*/directions**",
                                "/api/directions/*/topics**",
                                "/api/courses/*/comments**"
                        ).permitAll()

                        // Auth (signup/login) також без токену
                        .requestMatchers("/auth/**").permitAll()

                        // Всі інші — потребують токен
                        .anyRequest().authenticated()
                )
                // налаштовуємо JWT‐ресурсний сервер
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
        ;
        return http.build();
    }

    /**
     * Конвертер для читання ролей з токена Keycloak:
     * — бере їх із claim "realm_access.roles"
     * — додає префікс "ROLE_"
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // конвертер, який читає список ролей
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        // (опційно) прибрати дублювання Authority: ключ mapper
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authConverter.setPrincipalClaimName("preferred_username");
        authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authConverter;
    }
}
