package com.bright.auth_service.security;

import com.bright.auth_service.record.UserRecord;
import com.bright.auth_service.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public AppUserDetailsService(
            UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserRecord user = userService.getUserByEmail(email);

        return new AppUser(user.id(), user.email(), user.password(), "ROLE_USER");
    }

    public AppUser createUser(String email, String password) {
        UserRecord user = userService.createUser(email, null, password);

        return new AppUser(user.id(), user.email(), user.password(), "ROLE_USER");
    }

    public boolean existsByEmail(String email) {
        return userService.userExist(email);
    }
}
