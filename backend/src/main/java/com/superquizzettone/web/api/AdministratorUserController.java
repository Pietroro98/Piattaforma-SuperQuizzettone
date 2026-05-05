package com.superquizzettone.web.api;

import com.superquizzettone.dto.*;
import com.superquizzettone.model.User;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.security.dto.UsernameCheckResponseDTO;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.IdNotNullForInsertException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdministratorUserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final SecurityUtils securityUtils;

    public AdministratorUserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
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
    public ResponseEntity<ResponseJSON<Object>> createUser(@RequestBody @Valid UserDTO utenteInput) {
        if (utenteInput.getId() != null) {
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        }
        Optional<UsernameCheckResponseDTO> suggerimenti = getSuggerimenti(utenteInput.getUsername());

        User userInserito = userService.inserisciNuovo(utenteInput.buildUtenteModel(false));
        UserDTO responseData = UserDTO.buildUtenteDTOFromModel(userInserito);

        ResponseEntity<ResponseJSON<Object>> result;
        if(suggerimenti.isPresent()){
            result = ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseJSON.success(200, "Lo username inserito non è valido, oecco alcuni suggerimenti per te", suggerimenti));
        } else {
            result = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseJSON.success(201, "Utente creato con successo.", responseData));
        }
        return result;
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
    public ResponseEntity<ResponseJSON<UserUpdateDTO>> assignRole(@PathVariable Long id, @RequestBody Long roleId) {
        User utenteEsistente = userService.caricaSingoloUtente(id);
        if (utenteEsistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "L'utente con id: "+ id + " non esiste"));
        }

        userService.assegnaRuolo(utenteEsistente, id, roleId);
        UserUpdateDTO responseData = UserUpdateDTO.buildDTOFromModel(utenteEsistente);
        return ResponseEntity.ok(
                ResponseJSON.success(200,
                        "Ruolo dell'utente: " + responseData.getName() + " " +
                                responseData.getSurname() + "aggiornato con successo",
                        responseData
                )
        );
    }

    @PatchMapping("/revoke-role/{id}")
    public ResponseEntity<ResponseJSON<UserUpdateDTO>> revokeRole(@PathVariable Long id, @RequestBody Long roleId){
        UserUpdateDTO dto = userService.revocaRuolo(id, roleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "ruolo cambiato con successo", dto));
    }

    @PutMapping("/disableUser/{id}")
    public ResponseEntity<ResponseJSON<UserStatusDTO>> disableUser(@PathVariable Long id){

    }

    private Optional<UsernameCheckResponseDTO> getSuggerimenti (String username) {
        return securityUtils.checkUsername(username);
    }

    /*
    disableUser(id Long):
    endpoint: PUT/(PATCH) ../admin/disable/{id}
    */

}
