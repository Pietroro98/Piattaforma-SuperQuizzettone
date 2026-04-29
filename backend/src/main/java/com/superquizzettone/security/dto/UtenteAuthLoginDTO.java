package com.superquizzettone.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtenteAuthLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UtenteAuthLoginDTO() {
    }
}
