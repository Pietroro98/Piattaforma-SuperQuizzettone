package com.superquizzettone.web.api;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.UserDTO;
import com.superquizzettone.model.User;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.IdNotNullForInsertException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrator/user")
public class AdministratorUserController {

   private final UserService userService;

    public AdministratorUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseJSON<List<UserDTO>>> findAll() {
        List<UserDTO> responseData =  userService.listAllUtenti()
                .stream()
                .map(UserDTO::buildUtenteDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista utenti recuperata con successo.", responseData)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseJSON<UserDTO>> findById(@PathVariable Long id) {
        User user = userService.caricaSingoloUtente((id));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(user);

        return ResponseEntity.ok(ResponseJSON.success(200, "Utente recuperato con successo.", responseData)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseJSON<UserDTO>> create(@RequestBody @Valid UserDTO utenteInput) {
        if (utenteInput.getId() != null) {
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        }
        User userInserito = userService.inserisciNuovo(utenteInput.buildUtenteModel(false));
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(userInserito);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseJSON.success(201, "Utente creato con successo.", responseData));
    }

}
