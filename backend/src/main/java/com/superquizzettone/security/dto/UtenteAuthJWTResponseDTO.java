package com.superquizzettone.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtenteAuthJWTResponseDTO {

    private String token;
    private String type = "Bearer";
    private String username;

    public UtenteAuthJWTResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
