package com.superquizzettone.service.ruolo;
import com.superquizzettone.model.Ruolo;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RuoloServiceImpl implements RuoloService {

    @Autowired
    private RuoloRepository ruoloRepository;

    public List<Ruolo> listAll() {
        return (List<Ruolo>) ruoloRepository.findAll();
    }

    public Ruolo caricaSingoloElemento(Long id) {
        return ruoloRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void aggiorna(Ruolo ruoloInstance) {
        ruoloRepository.save(ruoloInstance);
    }

    @Transactional
    public void inserisciNuovo(Ruolo ruoloInstance) {
        ruoloRepository.save(ruoloInstance);
    }

    @Transactional
    public void rimuovi(Long idToRemove) {
       ruoloRepository.deleteById(idToRemove);
    }

    public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) {
        return ruoloRepository.findByDescrizioneAndCodice(descrizione, codice);
    }
}
