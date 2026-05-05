package com.superquizzettone.dto;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private LocalDate creationDate;

    private Double totalPoints;
    private Set<Role> roles;
    private Long[] roleIds;

    public UserDTO(Long id, String name, String surname, String username) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
    }

    public User buildUtenteModel(boolean includeId) {
        User model = new User();
        if (includeId) {
            model.setId(id);
        }
        model.setName(name);
        model.setSurname(surname);
        model.setUsername(username);
        model.setPassword(password);
        model.setCreationDate(creationDate);
        model.setTotalPoints(totalPoints);
        model.setRoles(roles);

        if (roleIds != null) {
            Set<Role> ruoli = Arrays.stream(roleIds).map(Role::new).collect(Collectors.toSet());
            model.setRoles(ruoli);
        }

        return model;
    }

    public static UserDTO buildUtenteDTOFromModel(User model) {
        UserDTO dto = new UserDTO(model.getId(), model.getName(), model.getSurname(), model.getUsername());
        dto.setCreationDate(model.getCreationDate());
        dto.setTotalPoints(model.getTotalPoints());
        dto.setRoles(model.getRoles());

        return dto;
    }

    public List<Role> getRuoli() {
        if (roleIds == null) {
            return null;
        }
        return Arrays.stream(roleIds).map(Role::new).collect(Collectors.toList());
    }
}
