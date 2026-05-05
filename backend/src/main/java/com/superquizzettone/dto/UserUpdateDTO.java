package com.superquizzettone.dto;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserUpdateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    private List<RoleDTO> roles;
    private Long[] rolesIds;

    public User buildUtenteModel(Long id) {
        User model = new User();
        model.setId(id);
        model.setName(name);
        model.setSurname(surname);
        model.setUsername(username);
        return model;
    }

    public static UserUpdateDTO buildDTOFromModel(User model){
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName(model.getName());
        dto.setSurname(model.getSurname());
        dto.setUsername(model.getUsername());
        dto.setRoles(RoleDTO.createRuoloDTOListFromModelSet(model.getRoles()));

        return dto;
    }

    public List<Role> getRuoli() {
        if (rolesIds != null) {
            return Arrays.stream(rolesIds).map(Role::new).collect(Collectors.toList());
        }
        if (roles != null) {
            return roles.stream()
                    .map(item -> item.getId() == null ? null : new Role(item.getId()))
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
