package com.superquizzettone.dto;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.Utente;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UtenteUpdateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    private List<RuoloDTO> ruolo;
    private Long[] ruoliIds;

    public Utente buildUtenteModel(Long id) {
        Utente model = new Utente();
        model.setId(id);
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        return model;
    }

    public List<Ruolo> getRuoli() {
        if (ruoliIds != null) {
            return Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toList());
        }
        if (ruolo != null) {
            return ruolo.stream()
                    .map(item -> item.getId() == null ? null : new Ruolo(item.getId()))
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
