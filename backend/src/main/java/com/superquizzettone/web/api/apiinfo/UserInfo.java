package com.superquizzettone.web.api.apiinfo;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.UserDTO;
import com.superquizzettone.dto.UserUpdateDTO;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import com.superquizzettone.security.dto.ChangePasswordDTO;
import com.superquizzettone.security.dto.UtenteInfoJWTResponseDTO;
import com.superquizzettone.service.utente.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utente")
public class UserInfo {

    private final UserService userService;

    public UserInfo(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/testSoloAdmin")
    public String test() {
        return "OK";
    }

    @GetMapping("/userInfo")
    public ResponseEntity<ResponseJSON<UtenteInfoJWTResponseDTO>> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userLoggato = userService.findByUsername(username);
        List<String> ruoli = userLoggato.getRoles().stream().map(Role::getCode).toList();

        UtenteInfoJWTResponseDTO responseData = new UtenteInfoJWTResponseDTO(
                userLoggato.getName(),
                userLoggato.getUsername(),
                userLoggato.getUsername(),
                userLoggato.getState(),
                ruoli,
                userLoggato.getAttempts(),
                userLoggato.getCreationDate(),
                userLoggato.getTotalPoints()
        );

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Informazioni utente recuperate con successo", responseData)
        );
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResponseJSON<Void>> changePassword(@RequestBody @Valid ChangePasswordDTO body) {
        userService.changePassword(body.getCurrentPassword(), body.getNewPassword(), body.getConfirmPassword());

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Password modificata con successo.", null)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseJSON<UserDTO>> updateMe(@RequestBody @Valid UserUpdateDTO body) {
        User userAggiornato = userService.aggiornaProfilo(body);
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(userAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Profilo aggiornato con successo.", responseData)
        );
    }
}
