package com.superquizzettone.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameRegisterCheckDTO {
    @NotBlank(message = "Lo username è obbligatorio")
    private String username;

    public UsernameRegisterCheckDTO() {
    }

    public UsernameRegisterCheckDTO(String username) {
        this.username = username;
    }
}
