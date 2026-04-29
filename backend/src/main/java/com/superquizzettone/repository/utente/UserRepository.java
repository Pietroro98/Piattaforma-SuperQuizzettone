package com.superquizzettone.repository.utente;
import com.superquizzettone.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    @Query("from User u left join fetch u.roles where u.id = ?1")
    Optional<User> findByIdConRuoli(Long id);

    User findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

}
