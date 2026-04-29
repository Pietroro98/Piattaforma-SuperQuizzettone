package com.superquizzettone.service.utente;
import com.superquizzettone.dto.UserUpdateDTO;
import com.superquizzettone.model.Role;
import com.superquizzettone.model.User;
import java.util.List;

public interface UserService {
    List<User> listAllUtenti();
    User caricaSingoloUtente(Long id);
    User caricaSingoloUtenteConRuoli(Long id);
    User aggiorna(User user, List<Role> ruoliItem);
    User inserisciNuovo(User user);
    User disabilita(Long id);
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    User aggiornaProfilo(UserUpdateDTO userUpdateDTO);
    void changePassword(String currentPassword, String newPassword, String confirmPassword);
}
