package com.superquizzettone.dto;

import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorUserUpdateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    private String password;

    private Long[] roleIds;

    public User buildUserModel(Long id) {
        User model = new User();
        model.setId(id);
        model.setName(name);
        model.setUsername(username);
        model.setUsername(username);
        model.setPassword(password);

        if (roleIds != null) {
            Set<Role> ruoli = Arrays.stream(roleIds)
                    .map(Role::new)
                    .collect(Collectors.toSet());
            model.setRoles(ruoli);
        }

        return model;
    }

    public List<Role> getRoles() {
        if (roleIds == null) {
            return null;
        }
        return Arrays.stream(roleIds).map(Role::new).collect(Collectors.toList());
    }
}
