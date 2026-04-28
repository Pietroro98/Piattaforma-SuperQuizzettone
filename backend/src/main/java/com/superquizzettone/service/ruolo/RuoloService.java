package com.superquizzettone.service.ruolo;

import com.superquizzettone.model.Ruolo;

import java.util.List;

public interface RuoloService {
    List<Ruolo> listAll();
    Ruolo caricaSingoloElemento(Long id);
    void aggiorna(Ruolo ruolo);
    void inserisciNuovo(Ruolo ruolo);
    void rimuovi(Long id);
    Ruolo cercaPerDescrizioneECodice(String descrizione, String codice);
}
