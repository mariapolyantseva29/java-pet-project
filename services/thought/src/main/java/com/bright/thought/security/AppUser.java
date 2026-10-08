package com.bright.thought.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AppUser implements UserDetails {
    private final UUID id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public AppUser(UUID id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = List.of(new SimpleGrantedAuthority(role));
    }

    public UUID getId() { return id; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
