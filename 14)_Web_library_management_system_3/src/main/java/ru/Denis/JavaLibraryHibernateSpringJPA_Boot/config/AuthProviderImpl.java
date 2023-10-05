package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;


@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final AdminDetailsService adminDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthProviderImpl(AdminDetailsService adminDetailsService, PasswordEncoder passwordEncoder) {
        this.adminDetailsService = adminDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String user_name = authentication.getName();
        String password = authentication.getCredentials().toString();

        AdminDetailsService.AdminDetails adminDetails = adminDetailsService.loadUserByUsername(user_name);

        if (!passwordEncoder.matches(password, adminDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password!");
        }

        return new UsernamePasswordAuthenticationToken(adminDetails, password,
                adminDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
