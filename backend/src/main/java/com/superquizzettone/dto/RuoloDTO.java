package com.superquizzettone.dto;

import com.superquizzettone.model.Ruolo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class RuoloDTO {

    private Long id;
    private String descrizione;
    private String codice;

    public static RuoloDTO buildRuoloDTOFromModel(Ruolo model) {
        return new RuoloDTO(model.getId(), model.getDescrizione(), model.getCodice());
    }

    public static List<RuoloDTO> createRuoloDTOListFromModelSet(Set<Ruolo> modelList) {
        return modelList.stream().map(RuoloDTO::buildRuoloDTOFromModel).toList();
    }

    public static List<RuoloDTO> createRuoloDTOListFromModelList(List<Ruolo> modelList) {
        return modelList.stream().map(RuoloDTO::buildRuoloDTOFromModel).toList();
    }
}
