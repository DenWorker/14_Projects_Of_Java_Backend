package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final AuthProviderImpl authProvider;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(AuthProviderImpl authProvider, JwtRequestFilter jwtRequestFilter) {
        this.authProvider = authProvider;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests
                        ((authorize) -> authorize
                                .requestMatchers("/auth/**", "/authREST/**", "/static/**",
                                        "/logout").permitAll()
                                .requestMatchers("/admins/admin").hasAnyRole("ADMIN_SIMPLE", "ADMIN_INTERPRETER", "ADMIN_MAIN")
                                .requestMatchers("/admins/translator").hasAnyRole("ADMIN_INTERPRETER", "ADMIN_MAIN")
                                .requestMatchers("/admins/**").hasRole("ADMIN_MAIN")
                                .anyRequest().hasAnyRole("ADMIN_SIMPLE", "ADMIN_INTERPRETER", "ADMIN_MAIN"))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(form -> form
                        .loginPage("/auth/login")
                        .failureUrl("/auth/error")
                        .defaultSuccessUrl("/books"))
                .httpBasic(withDefaults())
                .authenticationProvider(authProvider);

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login"));

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}