package com.superquizzettone.config;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.StatoUtente;
import com.superquizzettone.model.Utente;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import com.superquizzettone.repository.utente.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRuoli(RuoloRepository ruoloRepository, UtenteRepository utenteRepository,PasswordEncoder passwordEncoder)
    {
        return args -> {
            Ruolo administratorRole = createRole(ruoloRepository, "Amministratore", Ruolo.ROLE_ADMINISTRATOR);
            Ruolo writerRole = createRole(ruoloRepository, "Writer", Ruolo.ROLE_WRITER);
            Ruolo reviewerRole = createRole(ruoloRepository, "Reviewer", Ruolo.ROLE_REVIEWER);
            createRole(ruoloRepository, "Player", Ruolo.ROLE_PLAYER);
            createUser(utenteRepository, passwordEncoder, "Administrator", "System", "Administrator1", "Administrator_123", administratorRole);
            createUser(utenteRepository, passwordEncoder, "Writer", "System","Writer1", "Writer_123", writerRole);
            createUser(utenteRepository, passwordEncoder, "Reviewer", "System","Reviewer1", "organizer_123", reviewerRole);
        };
    }

    private Ruolo createRole(RuoloRepository ruoloRepository, String descrizione, String codice) {
        return ruoloRepository.findByCodice(codice)
            .orElseGet(() -> ruoloRepository.save(new Ruolo(descrizione, codice)));
    }

    private void createUser(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, String nome,
                            String cognome, String username, String rawPassword, Ruolo ruolo) {
        if (utenteRepository.existsByUsername(username)) {
            return;
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setUsername(username);
        utente.setPassword(passwordEncoder.encode(rawPassword));
        utente.setDataRegistrazione(LocalDate.now());
        utente.setStato(StatoUtente.ATTIVO);
        utente.setRuoli(Set.of(ruolo));
        utente.setTotalPoints(0d);
        utenteRepository.save(utente);
    }
}
