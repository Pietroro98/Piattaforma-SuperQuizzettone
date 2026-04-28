package com.superquizzettone.dto;

import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.Utente;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUtenteUpdateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    private String password;
    @Min(0)
    private Integer eloRating;

    @Min(0)
    private Double montePremi;

    private Long[] ruoliIds;
    private Long torneoId;

    public Utente buildUtenteModel(Long id) {
        Utente model = new Utente();
        model.setId(id);
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);

        if (ruoliIds != null) {
            Set<Ruolo> ruoli = Arrays.stream(ruoliIds)
                    .map(Ruolo::new)
                    .collect(Collectors.toSet());
            model.setRuoli(ruoli);
        }

        return model;
    }

    public List<Ruolo> getRuoli() {
        if (ruoliIds == null) {
            return null;
        }
        return Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toList());
    }
}
