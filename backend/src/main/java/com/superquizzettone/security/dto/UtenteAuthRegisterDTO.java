package com.superquizzettone.security.dto;
import com.superquizzettone.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UtenteAuthRegisterDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public User buildUtenteModel() {
        User model = new User();
        model.setName(name);
        model.setSurname(surname);
        model.setUsername(username);
        model.setPassword(password);
        return model;
    }

}
