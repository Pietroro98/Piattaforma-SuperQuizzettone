package com.superquizzettone.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @NotBlank(message = "La password attuale è obbligatoria.")
    private String currentPassword;

    @NotBlank(message = "La nuova password è obbligatoria.")
    private String newPassword;

    @NotBlank(message = "La conferma password è obbligatoria.")
    private String confirmPassword;


}
