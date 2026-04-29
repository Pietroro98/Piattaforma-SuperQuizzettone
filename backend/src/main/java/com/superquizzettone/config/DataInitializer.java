package com.superquizzettone.config;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.UserState;
import com.superquizzettone.model.User;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import com.superquizzettone.repository.utente.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRole(RuoloRepository ruoloRepository, UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        return args -> {
            Role administratorRole = createRole(ruoloRepository, "Amministratore", Role.ROLE_ADMINISTRATOR);
            Role writerRole = createRole(ruoloRepository, "Writer", Role.ROLE_WRITER);
            Role reviewerRole = createRole(ruoloRepository, "Reviewer", Role.ROLE_REVIEWER);
            createRole(ruoloRepository, "Player", Role.ROLE_PLAYER);
            createUser(userRepository, passwordEncoder, "Administrator", "System", "Administrator1", "Administrator_123", administratorRole);
            createUser(userRepository, passwordEncoder, "Writer", "System","Writer1", "Writer_123", writerRole);
            createUser(userRepository, passwordEncoder, "Reviewer", "System","Reviewer1", "organizer_123", reviewerRole);
        };
    }

    private Role createRole(RuoloRepository ruoloRepository, String descrizione, String codice) {
        return ruoloRepository.findByCode(codice)
            .orElseGet(() -> ruoloRepository.save(new Role(descrizione, codice)));
    }

    private void createUser(UserRepository userRepository, PasswordEncoder passwordEncoder, String name,
                            String surname, String username, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCreationDate(LocalDate.now());
        user.setState(UserState.ATTIVO);
        user.setRoles(Set.of(role));
        user.setTotalPoints(0d);
        userRepository.save(user);
    }
}
