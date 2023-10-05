package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.config;


import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.JwtTokenUtils;

import java.io.IOException;


// Это фильтр, который перехватывает входящий запрос.
// Он анализирует JWT (достаёт оттуда информацию).
@Component
// Каждый запрос будет перехвачен.
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final AdminDetailsService adminDetailsService;

    @Autowired
    public JwtRequestFilter(JwtTokenUtils jwtTokenUtils, AdminDetailsService adminDetailsService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.adminDetailsService = adminDetailsService;
    }


    // Аргументы - данные http запроса.
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Извлекаем хедер из запроса.
        // В этом хедере наш токен.
        String authHeader = httpServletRequest.getHeader("Authorization");


        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            // Из хедера достаём сам токен (если с хедером всё хорошо).
            String jwt = authHeader.substring(7);

            // Если самого токена в хедере нет, то выкидавыем BAD_REQUEST.
            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    // Уже с помощью реализованного класса по JWT (там генерация и валидация) обрабатываем токен.
                    String username = jwtTokenUtils.validateTokenAndRetrieveClaim(jwt);

                    // Из БД достаём данные, которые принадлежат username.
                    UserDetails userDetails = adminDetailsService.loadUserByUsername(username);


                    // Здесь уже происходит авторизация нашего пользователя.
                    // Создаём токен (новый) авторизации (содержаться важные поля пользователя).
                    // userDetails - для получения данных в будущем его добавление необходимо тоже.
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    // То есть по итогу по одним данным берём другие данные и полученный токен ложим в контекст спринг секурити.
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                    // Если что-то не так с токеном, то выкидываем ошибку.
                } catch (JWTVerificationException exception) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
            }
        }

        // Наш запрос мы продвигаем дальше по цепочке фильтров (их много).
        // Передаём служебную инфу.
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
