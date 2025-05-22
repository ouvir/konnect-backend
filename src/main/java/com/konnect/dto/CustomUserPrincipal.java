package com.konnect.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomUserPrincipal implements OAuth2User, UserDetails {
    private final UserDTO userDTO;

    // ------  OAuth2User  ------
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public Long getId() {
        return userDTO.getUserId();
    }


    // ------  UserDetails  ------
    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public String getEmail() {
        return userDTO.getEmail();
    }
}
