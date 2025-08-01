package com.kaptue.educamer.security;

import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.UserRepository;
import com.kaptue.educamer.service.PermissionMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionMappingService permissionMappingService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        // Obtenir les permissions granulaires (ex: "course:create")
        Set<String> permissionStrings = permissionMappingService.getPermissionsForUser(user);

        Set<GrantedAuthority> authorities = permissionStrings.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // Ajouter le RÔLE principal (ex: "ROLE_INSTRUCTEUR") comme autorité
        // Cela permet d'utiliser hasRole('INSTRUCTEUR') ou hasAuthority('ROLE_INSTRUCTEUR')
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getClass().getSimpleName().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}