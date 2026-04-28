package com.superquizzettone.service.utente;
import com.superquizzettone.dto.UtenteUpdateDTO;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.model.Utente;
import java.util.List;

public interface UtenteService {
    List<Utente> listAllUtenti();
    Utente caricaSingoloUtente(Long id);
    Utente caricaSingoloUtenteConRuoli(Long id);
    Utente aggiorna(Utente utente, List<Ruolo> ruoliItem);
    Utente inserisciNuovo(Utente utente);
    Utente disabilita(Long id);
    Utente findByUsernameAndPassword(String username, String password);
    Utente findByUsername(String username);
    Utente aggiornaProfilo(UtenteUpdateDTO utenteUpdateDTO);
    void changePassword(String currentPassword, String newPassword, String confirmPassword);
}
