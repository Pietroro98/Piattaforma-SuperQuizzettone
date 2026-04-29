package com.superquizzettone.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsernameCheckResponseDTO {

    private boolean available;
    private List<String> suggestions;

    public UsernameCheckResponseDTO() {
    }

    public UsernameCheckResponseDTO(boolean available, List<String> suggestions) {
        this.available = available;
        this.suggestions = suggestions;
    }
}
