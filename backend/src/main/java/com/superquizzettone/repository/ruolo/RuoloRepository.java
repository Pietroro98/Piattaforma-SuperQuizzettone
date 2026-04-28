package com.superquizzettone.repository.ruolo;
import com.superquizzettone.model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Ruolo findByDescrizioneAndCodice(String descrizione, String codice);
    Optional<Ruolo> findByCodice(String codice);
}
