package com.superquizzettone.web.api.apiinfo;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.UtenteDTO;
import com.superquizzettone.dto.UtenteUpdateDTO;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.Utente;
import com.superquizzettone.security.dto.ChangePasswordDTO;
import com.superquizzettone.security.dto.UtenteInfoJWTResponseDTO;
import com.superquizzettone.service.utente.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utente")
public class UserInfo {

    private final UtenteService utenteService;

    public UserInfo(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/testSoloAdmin")
    public String test() {
        return "OK";
    }

    @GetMapping("/userInfo")
    public ResponseEntity<ResponseJSON<UtenteInfoJWTResponseDTO>> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utenteLoggato = utenteService.findByUsername(username);
        List<String> ruoli = utenteLoggato.getRuoli().stream().map(Ruolo::getCodice).toList();

        UtenteInfoJWTResponseDTO responseData = new UtenteInfoJWTResponseDTO(
                utenteLoggato.getNome(),
                utenteLoggato.getCognome(),
                utenteLoggato.getUsername(),
                utenteLoggato.getStato(),
                ruoli,
                utenteLoggato.getAttempts(),
                utenteLoggato.getDataRegistrazione(),
                utenteLoggato.getTotalPoints()
        );

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Informazioni utente recuperate con successo", responseData)
        );
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseJSON<Void>> changePassword(@RequestBody @Valid ChangePasswordDTO body) {
        utenteService.changePassword(body.getCurrentPassword(), body.getNewPassword(), body.getConfirmPassword());

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Password modificata con successo.", null)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseJSON<UtenteDTO>> updateMe(@RequestBody @Valid UtenteUpdateDTO body) {
        Utente utenteAggiornato = utenteService.aggiornaProfilo(body);
        UtenteDTO responseData = UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Profilo aggiornato con successo.", responseData)
        );
    }
}
