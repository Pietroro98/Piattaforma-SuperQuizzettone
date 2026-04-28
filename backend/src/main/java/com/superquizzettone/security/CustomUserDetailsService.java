package com.superquizzettone.security;

import com.superquizzettone.model.Utente;
import com.superquizzettone.repository.utente.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));

        return new User(
            utente.getUsername(),
            utente.getPassword(),
            utente.isAttivo(),
            true,
            true,
            !utente.isDisabilitato(),
            getAuthorities(utente)
        );
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Utente utente) {
        String[] authorities = utente.getRuoli().stream().map(item -> item.getCodice()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }
}
