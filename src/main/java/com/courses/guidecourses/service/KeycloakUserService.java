package com.courses.guidecourses.service;

import javax.ws.rs.core.Response;

import com.courses.guidecourses.dto.UserSignupDto;
import com.courses.guidecourses.exception.UserAlreadyExistsException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;
    private final String realm;

    public KeycloakUserService(
            @Value("${keycloak.auth-server-url}") String serverUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret
    ) {
        this.realm = realm;
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    public String createUser(UserSignupDto dto) {

        RealmResource realmRes = keycloak.realm(realm);
        UsersResource users = realmRes.users();

        // 1) Формуємо новий UserRepresentation:
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRequiredActions(Collections.emptyList());

        // 2) Вбудовуємо пароль прямо у запит
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(dto.getPassword());
        cred.setTemporary(false);
        user.setCredentials(List.of(cred));

        // 3) Створюємо
        Response response = users.create(user);
        int status = response.getStatus();
        if (status == Response.Status.CONFLICT.getStatusCode()) {
            response.close();
            throw new UserAlreadyExistsException("Користувач уже існує");
        }
        if (status != Response.Status.CREATED.getStatusCode()) {
            response.close();
            throw new RuntimeException("Keycloak create user failed: HTTP " + status);
        }

        // 4) Дістаємо just-created ID (до close)
        String path = response.getLocation().getPath();   // …/users/{id}
        String userId = path.substring(path.lastIndexOf('/') + 1);
        response.close();

        // 5) Призначаємо роль
        RoleRepresentation roleRep = realmRes.roles().get("USER").toRepresentation();
        realmRes.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRep));

        return userId;
    }

    private String extractIdFromLocation(Response response) {
        // /realms/{realm}/users/{id}
        String path = response.getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }


    private void assignRealmRole(String userId, String roleName) {
        RealmResource realmRes = keycloak.realm(realm);
        RoleRepresentation roleRep = realmRes.roles()
                .get(roleName)
                .toRepresentation();

        RoleScopeResource rolesResource = realmRes
                .users()
                .get(userId)
                .roles()
                .realmLevel();

        rolesResource.add(List.of(roleRep));
    }
}
