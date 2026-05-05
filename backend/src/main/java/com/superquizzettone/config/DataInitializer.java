package com.superquizzettone.config;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.UserState;
import com.superquizzettone.model.User;
import com.superquizzettone.repository.ruolo.RoleRepository;
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
    CommandLineRunner initRole(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        return args -> {
            Role administratorRole = createRole(roleRepository, "Amministratore", Role.ROLE_ADMINISTRATOR);
            Role writerRole = createRole(roleRepository, "Writer", Role.ROLE_WRITER);
            Role reviewerRole = createRole(roleRepository, "Reviewer", Role.ROLE_REVIEWER);
            createRole(roleRepository, "Player", Role.ROLE_PLAYER);
            createUser(userRepository, passwordEncoder, "Administrator", "System", "Administrator1", "Administrator_123", administratorRole);
            createUser(userRepository, passwordEncoder, "Writer", "System","Writer1", "Writer_123", writerRole);
            createUser(userRepository, passwordEncoder, "Reviewer", "System","Reviewer1", "Reviewer_123", reviewerRole);
        };
    }

    private Role createRole(RoleRepository roleRepository, String descrizione, String codice) {
        return roleRepository.findByCode(codice)
            .orElseGet(() -> roleRepository.save(new Role(descrizione, codice)));
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
