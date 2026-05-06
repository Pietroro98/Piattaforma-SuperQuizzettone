package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.User;
import com.superquizzettone.model.UserState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class UserStatusDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    private UserState state;

    public User buildUtenteModel(Long id) {
        User model = new User();
        model.setId(this.id);
        model.setName(this.name);
        model.setSurname(this.surname);
        model.setState(this.state);
        return model;
    }

    public static UserStatusDTO buildDTOFromModel(User model){
        UserStatusDTO dto = new UserStatusDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setSurname(model.getSurname());
        dto.setState(model.getState());
        return dto;
    }
}
