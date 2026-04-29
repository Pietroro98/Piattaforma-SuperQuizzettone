package com.superquizzettone.repository.ruolo;
import com.superquizzettone.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Role, Long> {
    Role findByDescriptionAndCode(String description, String code);
    Optional<Role> findByCode(String code);
}
