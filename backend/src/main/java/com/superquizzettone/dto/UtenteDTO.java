package com.superquizzettone.dto;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.Utente;
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
public class UtenteDTO {

    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private LocalDate dataRegistrazione;

    private Double totalPoints;
    private List<RuoloDTO> ruoli;
    private Long[] ruoliIds;
    private Long torneoId;

    public UtenteDTO(Long id, String nome, String cognome, String username) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
    }

    public Utente buildUtenteModel(boolean includeId) {
        Utente model = new Utente();
        if (includeId) {
            model.setId(id);
        }
        model.setNome(nome);
        model.setCognome(cognome);
        model.setUsername(username);
        model.setPassword(password);
        model.setDataRegistrazione(dataRegistrazione);
        model.setTotalPoints(totalPoints);

        if (ruoliIds != null) {
            Set<Ruolo> ruoli = Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toSet());
            model.setRuoli(ruoli);
        }

        return model;
    }

    public static UtenteDTO buildUtenteDTOFromModel(Utente model) {
        UtenteDTO dto = new UtenteDTO(model.getId(), model.getNome(), model.getCognome(), model.getUsername());
        dto.setDataRegistrazione(model.getDataRegistrazione());
        dto.setTotalPoints(model.getTotalPoints());
        dto.setRuoli(RuoloDTO.createRuoloDTOListFromModelSet(model.getRuoli()));

        return dto;
    }

    public List<Ruolo> getRuoli() {
        if (ruoliIds == null) {
            return null;
        }
        return Arrays.stream(ruoliIds).map(Ruolo::new).collect(Collectors.toList());
    }

    public List<RuoloDTO> getRuolo() {
        return ruoli;
    }

    public void setRuolo(List<RuoloDTO> ruolo) {
        this.ruoli = ruolo;
    }
}
