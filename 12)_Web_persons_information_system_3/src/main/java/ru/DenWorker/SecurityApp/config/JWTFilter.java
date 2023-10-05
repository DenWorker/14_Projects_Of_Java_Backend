package ru.DenWorker.SecurityApp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.DenWorker.SecurityApp.security.JWTUtil;
import ru.DenWorker.SecurityApp.services.PersonDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// Это фильтр, который перехватывает входящий запрос.
// Он анализирует JWT (достаёт оттуда информацию).
@Component
// Каждый запрос будет перехвачен.
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
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

            // Если самого токена в хедере нет, то выкидываем BAD_REQUEST.
            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    // Уже с помощью реализованного класса по JWT (там генерация и валидация) обрабатываем токен.
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);

                    // Из БД достаём данные, которые принадлежат username.
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);


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
