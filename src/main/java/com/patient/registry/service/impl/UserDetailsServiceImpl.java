package com.patient.registry.service.impl;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DEMO: just two users. Replace with DB/LDAP as needed.
        if ("reader".equals(username)) {
            return User.builder()
                    .username("reader")
                    .password("{noop}readerpass") // {noop} for plaintext, encode in real apps!
                    .roles("READ")
                    .build();
        } else if ("writer".equals(username)) {
            return User.builder()
                    .username("writer")
                    .password("{noop}writerpass")
                    .roles("WRITE")
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}