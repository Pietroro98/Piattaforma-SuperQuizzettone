package com.superquizzettone.service.ruolo;
import com.superquizzettone.model.Role;
import com.superquizzettone.repository.ruolo.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RuoloRepository ruoloRepository;

    public List<Role> listAll() {
        return (List<Role>) ruoloRepository.findAll();
    }

    public Role caricaSingoloElemento(Long id) {
        return ruoloRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void aggiorna(Role roleInstance) {
        ruoloRepository.save(roleInstance);
    }

    @Transactional
    public void inserisciNuovo(Role roleInstance) {
        ruoloRepository.save(roleInstance);
    }

    @Transactional
    public void rimuovi(Long idToRemove) {
       ruoloRepository.deleteById(idToRemove);
    }

    public Role cercaPerDescrizioneECodice(String descrizione, String codice) {
        return ruoloRepository.findByDescriptionAndCode(descrizione, codice);
    }
}
