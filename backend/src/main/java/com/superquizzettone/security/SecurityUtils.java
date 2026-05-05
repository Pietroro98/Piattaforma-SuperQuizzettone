package com.superquizzettone.security;
import com.superquizzettone.model.User;
import com.superquizzettone.repository.utente.UserRepository;
import com.superquizzettone.security.dto.UsernameCheckResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    private static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> role.equals(auth.getAuthority()));
    }

    public static boolean isAdministrator() {
        return hasRole("ROLE_ADMINISTRATOR");
    }

    public static boolean isPlayer() {
        return hasRole("ROLE_PLAYER");
    }

    public static boolean isWriter() {
        return hasRole("ROLE_WRITER");
    }

    public static boolean isReviewer() {
        return hasRole("ROLE_REVIEWER");
    }

    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }
        throw new IllegalStateException("Il principal autenticato non espone di un id utente valido.");
    }

    private static final String DEFAULT_P = "player";
    private static final int MAX_SUGGERITI = 3;

    /**
     * Metodo che costruisce una lista di suggerimenti per username alternativi quando quello richiesto è già in uso.
     * Utilizza una base normalizzata del nome richiesto e aggiunge numeri,
     * l'anno corrente, suffissi  e prefissi per generare varianti plausibili.
     * @param username
     * @return
     */
    public List<String> buildUsernameSuggeriti(String username) {
        String base = normalizeUsername(username);
        int year = Year.now().getValue();

        List<String> candidates = List.of(
                base + "1",
                base + "10",
                base + "123",
                base + "_" + year,
                base + "_" + (year + 1),
                base + "_01",
                base + "_99",
                base + "Chess",
                "real_" + base,
                base + "_official"
        );

        return candidates.stream()
                .filter(candidate -> !userRepository.existsByUsername(candidate))
                .limit(MAX_SUGGERITI)
                .toList();
    }

    /**
     * metodo che normalizza l'username rimuovendo spazi,
     * caratteri speciali e underscore finali,
     * per costruire suggerimenti più puliti e validi.
     * @param username
     * @return
     */
    private static String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return DEFAULT_P;
        }

        String base = username.trim()
                .replaceAll("[^A-Za-z0-9_]", "")
                .replaceAll("_+$", "");

        return base.isBlank() ? DEFAULT_P : base;
    }

    public Optional<UsernameCheckResponseDTO> checkUsername (String username){
        boolean exists = userRepository.existsByUsername(username);

        if (exists) {
            UsernameCheckResponseDTO responseData =
                    new UsernameCheckResponseDTO(false, this.buildUsernameSuggeriti(username));


            return Optional.of(responseData);
        }
        return Optional.empty();
    }
}
