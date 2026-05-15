package com.superquizzettone.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UtenteAuthLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private List<String> roles;

    public UtenteAuthLoginDTO() {
    }
}
