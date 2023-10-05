package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllersREST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.config.AuthProviderImpl;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto.AuthAdminDTO;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.JwtTokenUtils;

import java.util.Map;

@RestController
@RequestMapping("/authREST")
public class AuthControllerREST {
    private final AuthProviderImpl authProvider;
    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public AuthControllerREST(AuthProviderImpl authProvider, JwtTokenUtils jwtTokenUtils) {
        this.authProvider = authProvider;
        this.jwtTokenUtils = jwtTokenUtils;
    }


    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthAdminDTO authAdminDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authAdminDTO.getUsername(), authAdminDTO.getPassword());
        try {
            authProvider.authenticate(authenticationToken);
        } catch (BadCredentialsException exception) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtTokenUtils.generateToken(authAdminDTO.getUsername());
        return Map.of("jwt-token", token);
    }

}
