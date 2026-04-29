package com.superquizzettone.dto;

import com.superquizzettone.model.Role;
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
public class RoleDTO {

    private Long id;
    private String description;
    private String code;

    public static RoleDTO buildRuoloDTOFromModel(Role model) {
        return new RoleDTO(model.getId(), model.getDescription(), model.getCode());
    }

    public static List<RoleDTO> createRuoloDTOListFromModelSet(Set<Role> modelList) {
        return modelList.stream().map(RoleDTO::buildRuoloDTOFromModel).toList();
    }

    public static List<RoleDTO> createRuoloDTOListFromModelList(List<Role> modelList) {
        return modelList.stream().map(RoleDTO::buildRuoloDTOFromModel).toList();
    }
}
