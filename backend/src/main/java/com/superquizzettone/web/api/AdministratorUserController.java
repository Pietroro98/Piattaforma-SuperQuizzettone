package com.superquizzettone.web.api;

import com.superquizzettone.dto.*;
import com.superquizzettone.model.User;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.IdNotNullForInsertException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdministratorUserController {

    private final UserService userService;

    public AdministratorUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list-users")
    public ResponseEntity<ResponseJSON<List<UserDTO>>> usersList() {
        List<UserDTO> responseData = userService.listAllUtenti()
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

    @PostMapping("/create")
    public ResponseEntity<ResponseJSON<UserDTO>> createUser(@RequestBody @Valid UserDTO utenteInput) {
        if (utenteInput.getId() != null) {
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        }
        User userInserito = userService.inserisciNuovo(utenteInput.buildUtenteModel(false));
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(userInserito);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseJSON.success(201, "Utente creato con successo.", responseData));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ResponseJSON<UserDTO>> modifyUser(@PathVariable Long id, @RequestBody @Valid AdministratorUserUpdateDTO utenteInput) {
        User utenteEsistente = userService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.notFound().build();
        }
        User utenteAggiornato = userService.aggiornaComeAdmin(utenteInput, id);
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(utenteAggiornato);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Utente aggiornato con successo.", responseData)
        );
    }

    @PutMapping("/deleteUser/{id}")
    public ResponseEntity<ResponseJSON<UserDTO>> disabilita(@PathVariable Long id) {
        User utenteEsistente = userService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(ResponseJSON.error(404, "L'utente con id: "+ id + " non esiste"));
        }
        userService.disabilita(id);
        return ResponseEntity.ok(
                ResponseJSON.success(200, "Utente disabilitato con successo.", null)
        );
    }

    @PatchMapping("/assign-role/{id}")
    public ResponseEntity<ResponseJSON<UserUpdateDTO>> assignRole(@PathVariable Long id, RoleDTO roleDTO) {
        User utenteEsistente = userService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "L'utente con id: "+ id + " non esiste"));
        }

        userService.assegnaRuolo(utenteEsistente, id, RoleDTO.createModelFromDTO(roleDTO));
        UserUpdateDTO responseData = UserUpdateDTO.buildDTOFromModel(utenteEsistente);
        return ResponseEntity.ok(
                ResponseJSON.success(200,
                        "Ruolo dell'utente: " + responseData.getName() + " " +
                                responseData.getSurname() + "aggiornato con successo",
                        responseData
                )
        );
    }
    /*
    @PatchMapping("/revoke-role/{id}")
    public ResponseEntity<ResponseJSON<UserUpdateDTO>> revokeRole(@PathVariable Long id, RoleDTO roleDTO){

    }

     */
/*

    revokeRole(Body Role)
    endpoint: PATCH ../admin/revoke-role/{id}
    disableUser(id Long):
    endpoint: PUT/(PATCH) ../admin/disable/{id}
*/

}
