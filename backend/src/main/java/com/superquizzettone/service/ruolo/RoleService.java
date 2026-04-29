package com.superquizzettone.service.ruolo;

import com.superquizzettone.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> listAll();
    Role caricaSingoloElemento(Long id);
    void aggiorna(Role role);
    void inserisciNuovo(Role role);
    void rimuovi(Long id);
    Role cercaPerDescrizioneECodice(String descrizione, String codice);
}
