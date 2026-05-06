package com.superquizzettone.service.utente;

import com.superquizzettone.dto.AdministratorUserUpdateDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.RoleDTO;
import com.superquizzettone.dto.UserUpdateDTO;
import com.superquizzettone.model.*;
import com.superquizzettone.repository.ruolo.RoleRepository;
import com.superquizzettone.repository.utente.UserRepository;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.security.dto.UsernameCheckResponseDTO;
import com.superquizzettone.service.ruolo.RoleService;
import com.superquizzettone.web.api.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> listAllUtenti() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User caricaSingoloUtente(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User caricaSingoloUtenteConRuoli(Long id) {
        return userRepository.findByIdConRuoli(id).orElse(null);
    }

    @Override
    @Transactional
    public User aggiorna(User userInstance, List<Role> ruoliItem) {
        User userReloaded = userRepository.findById(userInstance.getId()).orElse(null);
        if (userReloaded == null) {
            throw new RuntimeException("Elemento non trovato");
        }

        boolean isAdmin = SecurityUtils.isAdministrator();
        String usernameLoggato = SecurityUtils.getUsername();

        if (!isAdmin && !usernameLoggato.equals(userReloaded.getUsername())) {
            throw new ForbiddenException(usernameLoggato);
        }

        userReloaded.setName(userInstance.getName());
        userReloaded.setUsername(userInstance.getUsername());
        userReloaded.setUsername(userInstance.getUsername());

        if (!isAdmin) {
            return userRepository.save(userReloaded);
        }

        if (ruoliItem == null || ruoliItem.isEmpty()) {
            throw new BadRequestException("Devi specificare almeno un ruolo");
        }

        Set<Role> ruoliValidi = ruoliItem
                .stream()
                .map(ruoloInput -> {
                    if (ruoloInput.getId() == null) {
                        throw new BadRequestException("Ogni ruolo deve avere un id");
                    }

                    return roleRepository.findById(ruoloInput.getId())
                            .orElseThrow(() -> new BadRequestException(
                                    "Ruolo non valido con id: " + ruoloInput.getId()));
                }).collect(Collectors.toSet());

        boolean contieneAdmin = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        Role.ROLE_ADMINISTRATOR.equals(ruolo.getCode())
                );

        if (contieneAdmin) {
            throw new NotAllowedException("Non è consentito assegnare il ruolo ADMIN");
        }

        userReloaded.setRoles(ruoliValidi);

        return userRepository.save(userReloaded);
    }

    @Override
    @Transactional
    public User aggiornaComeAdmin(AdministratorUserUpdateDTO userUpdateDTO, Long id) {
        User utenteReloaded = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Elemento non trovato."));
        String usernameLoggato = SecurityUtils.getUsername();

        if (utenteReloaded.isAdmin() && !usernameLoggato.equals(utenteReloaded.getUsername())) {
            throw new ForbiddenException("Non puoi modificare un altro utente admin.");
        }

        utenteReloaded.setName(userUpdateDTO.getName());
        utenteReloaded.setSurname(userUpdateDTO.getSurname());
        utenteReloaded.setUsername(userUpdateDTO.getUsername());
        utenteReloaded.setState(userUpdateDTO.getState());
        utenteReloaded.setCreationDate(userUpdateDTO.getCreationDate());
        utenteReloaded.setTotalPoints(userUpdateDTO.getTotalPoints());

        if (userUpdateDTO.getAttempts() != null) {
            utenteReloaded.setAttempts(userUpdateDTO.getAttempts());
        }

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            utenteReloaded.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        if (userUpdateDTO.getRoles() == null || userUpdateDTO.getRoles().isEmpty()) {
            throw new BadRequestException("Devi specificare almeno un ruolo");
        }

        Set<Role> ruoliValidi = userUpdateDTO.getRoles()
                .stream()
                .map(ruoloInput -> {
                    if (ruoloInput.getId() == null) {
                        throw new BadRequestException("Ogni ruolo deve avere un id");
                    }

                    return roleRepository.findById(ruoloInput.getId())
                            .orElseThrow(() -> new BadRequestException(
                                    "Ruolo non valido con id: " + ruoloInput.getId()));
                }).collect(Collectors.toSet());

        boolean contieneAdmin = ruoliValidi.stream()
                .anyMatch(ruolo -> Role.ROLE_ADMINISTRATOR.equals(ruolo.getCode()));

        if (contieneAdmin && !utenteReloaded.isAdmin()) {
            throw new NotAllowedException("Non è consentito assegnare il ruolo ADMIN");
        }

        utenteReloaded.setRoles(ruoliValidi);

        return userRepository.save(utenteReloaded);
    }

    @Override
    @Transactional
    public User assegnaRuolo(User user, Long id, Long roleId) {

        if(roleId == null){
            throw new NotFoundException("Id del ruolo non pervenuto");
        }
        Role role = roleService.caricaSingoloElemento(roleId);
        if (role == null){
            throw new NotFoundException("Ruolo inesistente");
        }

        boolean alreadyHasRole = user.getRoles().stream()
                .anyMatch(r -> r.getId().equals(role.getId()));

        if (alreadyHasRole) {
            throw new BadRequestException("L'utente possiede già il ruolo scelto");
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserUpdateDTO revocaRuolo(Long id, Long roleId) {
        User entity = userRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Utente con id: " + id + " non disponibile");
        }
        if (entity.getRoles() == null) {
            throw new BadRequestException("Impossibile trovare il ruolo da revocare");
        }
        if (entity.getRoles().size() == 1) {
            throw new ForbiddenException("Impossibile togliere ruoli ad un utente che ne ha solo 1");
        }
        Role ruoloDaTogliere = roleService.caricaSingoloElemento(roleId);
        boolean isAdmin = entity.getRoles().stream()
                .anyMatch(r -> r.getId().equals(ruoloDaTogliere.getId()));
        if (isAdmin) {
            throw new ForbiddenException("Impossibile togliere il ruolo ad un altro admin");
        }
        entity.getRoles().remove(ruoloDaTogliere);
        userRepository.save(entity);
        return UserUpdateDTO.buildDTOFromModel(entity);
    }

    @Override
    public User abilita(Long id) {
        User entity = caricaSingoloUtente(id);
        if (entity == null) {
            throw new RuntimeException("Utente non trovato.");
        }

        if(!entity.isDisabled()){
            throw new AlreadyEnabledException("Utente già disabilitato");
        }
        entity.setState(UserState.ATTIVO);
        return userRepository.save(entity);
    }

    @Override
    @Transactional
    public User inserisciNuovo(User entity) {

        entity.setState(UserState.ATTIVO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setCreationDate(LocalDate.now());
        entity.setTotalPoints(0d);
        entity.setAttempts(List.of());
        entity.setMyQuiz(List.of());

        if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
            throw new EmptyRoleException("L'utente deve avere almeno un ruolo.");
        }

        Set<Role> ruoliValidi = entity.getRoles().stream()
                .map(ruoloInput -> roleRepository.findById(ruoloInput.getId())
                        .orElseThrow(() -> new BadRequestException(
                                "Ruolo non valido con id: " + ruoloInput.getId())))
                .collect(Collectors.toSet());

        boolean contieneRuoloNonConsentito = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        !Role.ROLE_ADMINISTRATOR.equals(ruolo.getCode()) &&
                                !Role.ROLE_PLAYER.equals(ruolo.getCode())
                );

        if (contieneRuoloNonConsentito) {
            throw new NotAllowedException("L'admin può creare solo utenti con ruolo PLAYER, WRITER, REVIEWER");
        }

        entity.setRoles(ruoliValidi);

        return userRepository.save(entity);
    }

    @Override
    @Transactional
    public User disabilita(Long id) {
        User entity = caricaSingoloUtente(id);
        if (entity == null) {
            throw new RuntimeException("Utente non trovato.");
        }

        if(entity.isDisabled()){
            throw new AlreadyDisabledException("Utente già disabilitato");
        }
        entity.setState(UserState.DISABILITATO);
        return userRepository.save(entity);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public User aggiornaProfilo(UserUpdateDTO userUpdateDTO) {
        String username = SecurityUtils.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente autenticato non trovato."));

        user.setName(userUpdateDTO.getName());
        user.setSurname(userUpdateDTO.getSurname());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        String username = SecurityUtils.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente autenticato non trovato."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("La password attuale non è corretta.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("La nuova password e la conferma password non coincidono.");
        }

        if (newPassword.equals(currentPassword)) {
            throw new BadRequestException("La nuova password deve essere diversa da quella attuale.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
