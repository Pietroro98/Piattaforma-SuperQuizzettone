package com.superquizzettone.security.dto;
import com.superquizzettone.model.User;
import com.superquizzettone.security.SanitizerUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UtenteAuthRegisterDTO {

    private Long id;

    @NotBlank(message = "Il nome non può essere nullo, vuoto o contenere solo spazi")
    private String name;

    @NotBlank(message = "Il cognome non può essere nullo, vuoto o contenere solo spazi")
    @Size(min = 4, message = "Lo username deve avere almeno 4 caratteri")
    private String surname;

    @NotBlank(message = "non può essere nullo, vuoto o contenere solo spazi")

    private String username;

    @NotBlank(message = "Il nome non può essere nullo, vuoto o contenere solo spazi")
    @Size(min = 6, max = 12, message = "La password deve avere tra 6 e 12 caratteri")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-zA-Z0-9])(?=.*[!£&$?_-])[A-Za-z0-9!£&$?_-]{6,12}$",
            message = "La password deve contenere almeno una maiuscola, caratteri alfanumerici e un carattere speciale (!£&$?_-)"
    )
    private String password;

    public User buildUtenteModel() {
        User model = new User();
        model.setName(SanitizerUtil.sanitize(name));
        model.setSurname(SanitizerUtil.sanitize(surname));
        model.setUsername(SanitizerUtil.sanitize(username));
        model.setPassword(password);
        return model;
    }

}
