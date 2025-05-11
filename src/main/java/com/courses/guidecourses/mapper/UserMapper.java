package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.UserDto;
import com.courses.guidecourses.dto.UserSignupDto;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.dto.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /** User → UserDto, перетворюємо Set<Role> → Set<String> */
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToStrings")
    UserDto toDto(User user);

    /** UserSignupDto → нова сутність User */
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "keycloakId",  ignore = true)
    @Mapping(target = "roles",       ignore = true)
    @Mapping(target = "createdAt",   ignore = true)
    User toEntity(UserSignupDto dto);

    /** Допоміжний метод: Role → String */
    @Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<Role> roles) {
        return roles == null
                ? Set.of()
                : roles.stream()
                .map(r -> r.name().replaceFirst("^ROLE_", ""))
                .collect(Collectors.toSet());
    }
}
