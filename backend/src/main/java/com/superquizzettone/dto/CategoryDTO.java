package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Il nome non può essere nullo")
    private String name;

    @NotBlank(message = "La descrizione non può essere nulla")
    private String description;

    public CategoryDTO() {}

    public static CategoryDTO buildDTOFromModel(Category model) {
        return new CategoryDTO(
                model.getId(),
                model.getName(),
                model.getDescription()
        );
    }

    public static Category buildModelFromDTO(CategoryDTO dto) {
        Category result = new Category();
        result.setName(dto.getName());
        result.setDescription(dto.getDescription());
        return result;
    }

    public static List<CategoryDTO> buildListDTOFromModel(List<Category> listModel) {
        return listModel.stream()
                .map(CategoryDTO::buildDTOFromModel)
                .toList();
    }

    public static List<Category> buildListModelFromDTO(List<CategoryDTO> listDTO) {
        return listDTO.stream()
                .map(CategoryDTO::buildModelFromDTO)
                .toList();
    }

}
