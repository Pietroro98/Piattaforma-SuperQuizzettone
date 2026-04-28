package com.superquizzettone.security.dto;
import com.superquizzettone.model.Utente;
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
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public Utente buildUtenteModel() {
        Utente model = new Utente();
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);
        return model;
    }

}
