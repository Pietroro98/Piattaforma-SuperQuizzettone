package com.superquizzettone.repository.utente;
import com.superquizzettone.model.Utente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    @EntityGraph(attributePaths = "ruoli")
    Optional<Utente> findByUsername(String username);

    @Query("from Utente u left join fetch u.ruoli where u.id = ?1")
    Optional<Utente> findByIdConRuoli(Long id);

    Utente findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

}
