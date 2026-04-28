package com.superquizzettone.service.utente;
import com.superquizzettone.dto.UtenteUpdateDTO;
import com.superquizzettone.model.*;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import com.superquizzettone.repository.utente.UtenteRepository;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.ForbiddenException;
import com.superquizzettone.web.api.exception.NotAllowedException;
import com.superquizzettone.web.api.exception.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloRepository ruoloRepository;

    public UtenteServiceImpl(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder,
                             RuoloRepository ruoloRepository) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.ruoloRepository = ruoloRepository;
    }

    @Override
    public List<Utente> listAllUtenti() {
        return (List<Utente>) utenteRepository.findAll();
    }

    @Override
    public Utente caricaSingoloUtente(Long id) {
        return utenteRepository.findById(id).orElse(null);
    }

    @Override
    public Utente caricaSingoloUtenteConRuoli(Long id) {
        return utenteRepository.findByIdConRuoli(id).orElse(null);
    }

    @Override
    @Transactional
    public Utente aggiorna(Utente utenteInstance, List<Ruolo> ruoliItem) {
        Utente utenteReloaded = utenteRepository.findById(utenteInstance.getId()).orElse(null);
        if (utenteReloaded == null) {
            throw new RuntimeException("Elemento non trovato");
        }

        boolean isAdmin = SecurityUtils.isAdministrator();
        String usernameLoggato = SecurityUtils.getUsername();

        if (!isAdmin && !usernameLoggato.equals(utenteReloaded.getUsername())) {
            throw new ForbiddenException(usernameLoggato);
        }

        utenteReloaded.setNome(utenteInstance.getNome());
        utenteReloaded.setCognome(utenteInstance.getCognome());
        utenteReloaded.setUsername(utenteInstance.getUsername());

        if (!isAdmin) {
            return utenteRepository.save(utenteReloaded);
        }

        if (ruoliItem == null || ruoliItem.isEmpty()) {
            throw new BadRequestException("Devi specificare almeno un ruolo");
        }

        Set<Ruolo> ruoliValidi = ruoliItem
                .stream()
                .map(ruoloInput -> {
                    if (ruoloInput.getId() == null) {
                        throw new BadRequestException("Ogni ruolo deve avere un id");
                    }

                    return ruoloRepository.findById(ruoloInput.getId())
                            .orElseThrow(() -> new BadRequestException(
                                    "Ruolo non valido con id: " + ruoloInput.getId()));
                }).collect(Collectors.toSet());

        boolean contieneAdmin = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        Ruolo.ROLE_ADMINISTRATOR.equals(ruolo.getCodice())
                );

        if (contieneAdmin) {
            throw new NotAllowedException("Non è consentito assegnare il ruolo ADMIN");
        }

        utenteReloaded.setRuoli(ruoliValidi);

        return utenteRepository.save(utenteReloaded);
    }

    @Override
    public Utente inserisciNuovo(Utente entity)
    {
        entity.setStato(StatoUtente.ATTIVO);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDataRegistrazione(LocalDate.now());
        entity.setTotalPoints(0d);

        if (entity.getRuoli() == null || entity.getRuoli().isEmpty()) {
            throw new RuntimeException("L'utente deve avere almeno un ruolo.");
        }

        Set<Ruolo> ruoliValidi = entity.getRuoli().stream()
                .map(ruoloInput -> ruoloRepository.findById(ruoloInput.getId())
                        .orElseThrow(() -> new BadRequestException(
                                "Ruolo non valido con id: " + ruoloInput.getId())))
                .collect(Collectors.toSet());

        boolean contieneRuoloNonConsentito = ruoliValidi.stream()
                .anyMatch(ruolo ->
                        !Ruolo.ROLE_ADMINISTRATOR.equals(ruolo.getCodice()) &&
                                !Ruolo.ROLE_PLAYER.equals(ruolo.getCodice())
                );

        if (contieneRuoloNonConsentito) {
            throw new NotAllowedException("L'admin può creare solo utenti con ruolo PLAYER, WRITER, REVIEWER");
        }

        entity.setRuoli(ruoliValidi);

        return utenteRepository.save(entity);
    }

    @Override
    public Utente disabilita(Long id) {
        Utente entity = caricaSingoloUtente(id);
        if (entity == null) {
            throw new RuntimeException("Elemento non trovato.");
        }
        entity.setStato(StatoUtente.DISABILITATO);
        return utenteRepository.save(entity);
    }

    @Override
    public Utente findByUsernameAndPassword(String username, String password) {
        return utenteRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public Utente aggiornaProfilo(UtenteUpdateDTO utenteUpdateDTO) {
        String username = SecurityUtils.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente autenticato non trovato."));

        utente.setNome(utenteUpdateDTO.getNome());
        utente.setCognome(utenteUpdateDTO.getCognome());
        utente.setUsername(utenteUpdateDTO.getUsername());

        return utenteRepository.save(utente);
    }

    @Override
    @Transactional
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        String username = SecurityUtils.getUsername();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente autenticato non trovato."));

        if (!passwordEncoder.matches(currentPassword, utente.getPassword())) {
            throw new BadRequestException("La password attuale non è corretta.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("La nuova password e la conferma password non coincidono.");
        }

        if (newPassword.equals(currentPassword)) {
            throw new BadRequestException("La nuova password deve essere diversa da quella attuale.");
        }

        utente.setPassword(passwordEncoder.encode(newPassword));
        utenteRepository.save(utente);
    }
}
