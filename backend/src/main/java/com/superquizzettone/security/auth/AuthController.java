package com.superquizzettone.security.auth;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import com.superquizzettone.repository.utente.UserRepository;
import com.superquizzettone.security.JWTUtil;
import com.superquizzettone.security.dto.*;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //scrivo una riga a livello informativo nei log del backend quando authenticate() della security fallisce
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RuoloRepository ruoloRepository;

    public AuthController(JWTUtil jwtUtil, AuthenticationManager authManager, UserService userService, UserRepository userRepository, RuoloRepository ruoloRepository) {
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.ruoloRepository = ruoloRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseJSON<UtenteAuthJWTResponseDTO>> loginHandler(@RequestBody @Valid UtenteAuthLoginDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getUsername());
            UtenteAuthJWTResponseDTO responseData =
                    new UtenteAuthJWTResponseDTO(token, body.getUsername());

            return ResponseEntity.ok(
                    ResponseJSON.success(200, "Login effettuato con successo", responseData)
            );
        } catch (AuthenticationException authExc) {
            log.warn("Login fallito per username '{}': {}", body.getUsername(), authExc.getClass().getSimpleName());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteAuthJWTResponseDTO> register(@RequestBody @Valid UtenteAuthRegisterDTO body) {
        if (body.getId() != null)
        {
            throw new BadRequestException("Attenzione, l'id non deve essere valorizzato in inserimento");
        }

        Role defaultRole = ruoloRepository.findByCode(Role.ROLE_PLAYER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ruolo PLAYER non configurato"));

        User entity = body.buildUtenteModel();
        entity.setRoles(Set.of(defaultRole));
        userService.inserisciNuovo(entity);

        String token = jwtUtil.generateToken(entity.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new UtenteAuthJWTResponseDTO(token, entity.getUsername()));
    }

    @PostMapping("/check-username")
    public ResponseEntity<ResponseJSON<UsernameCheckResponseDTO>> usernameCheck(@RequestBody @Valid UsernameRegisterCheckDTO body) {
        boolean exists = userRepository.existsByUsername(body.getUsername());

        if (exists) {
            UsernameCheckResponseDTO responseData =
                    new UsernameCheckResponseDTO(false, buildUsernameSuggeriti(body.getUsername()));

            return ResponseEntity.ok(
                    ResponseJSON.success(200, "Username non disponibile", responseData)
            );
        }

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Username disponibile", new UsernameCheckResponseDTO(true, List.of()))
        );
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
    private List<String> buildUsernameSuggeriti(String username) {
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
    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return DEFAULT_P;
        }

        String base = username.trim()
                .replaceAll("[^A-Za-z0-9_]", "")
                .replaceAll("_+$", "");

        return base.isBlank() ? DEFAULT_P : base;
    }
}
