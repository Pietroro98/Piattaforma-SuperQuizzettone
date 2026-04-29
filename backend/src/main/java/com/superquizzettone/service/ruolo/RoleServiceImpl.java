package com.superquizzettone.service.ruolo;
import com.superquizzettone.model.Role;
import com.superquizzettone.repository.ruolo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> listAll() {
        return (List<Role>) roleRepository.findAll();
    }

    public Role caricaSingoloElemento(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void aggiorna(Role roleInstance) {
        roleRepository.save(roleInstance);
    }

    @Transactional
    public void inserisciNuovo(Role roleInstance) {
        roleRepository.save(roleInstance);
    }

    @Transactional
    public void rimuovi(Long idToRemove) {
       roleRepository.deleteById(idToRemove);
    }

    public Role cercaPerDescrizioneECodice(String descrizione, String codice) {
        return roleRepository.findByDescriptionAndCode(descrizione, codice);
    }
}
