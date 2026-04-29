package com.superquizzettone.security;

import com.superquizzettone.model.User;
import com.superquizzettone.repository.utente.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            !user.isDisabled(),
            getAuthorities(user)
        );
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] authorities = user.getRoles().stream().map(item -> item.getCode()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }
}
