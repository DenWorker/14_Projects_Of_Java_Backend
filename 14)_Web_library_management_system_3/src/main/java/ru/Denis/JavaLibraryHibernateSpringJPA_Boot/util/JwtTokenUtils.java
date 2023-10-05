package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;


// Этот класс работает с JWT токенами.
@Component
public class JwtTokenUtils {

    // Метод, который генерирует токен.
    // Генерация на основании имени пользователя.
    public String generateToken(String username) {
        // Срок годности токена 60 минут.
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        // Создаём сам токен.
        // Этот JWT отправится клиенту, и он будет его хранить.
        // Так помещаем любые данные и сколько угодно в сам токен.
        // .withClaim("username", username);
        return JWT.create()
                // Что хранится в этом JWT токене (данные пользователя).
                .withSubject("User details")
                // Что ложится в тело токена (ключ-значение).
                .withClaim("username", username)
                // Когда токен был выдан.
                .withIssuedAt(new Date())
                // Кем токен был выдан.
                .withIssuer("Denis")
                // Когда токен уничтожится.
                .withExpiresAt(expirationDate)
                // Кодовое слово (лежит во внешнем файле).
                .sign(Algorithm.HMAC256("SecretWord2323"));
    }

    // Метод, который проверяет токен.
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        // Проверка кодового слова.
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("SecretWord2323"))
                // Какой должен быть subject.
                .withSubject("User details")
                // Кем должен быть выдан токен.
                .withIssuer("Denis")
                .build();

        // Сама валидация токена, который был передан аргументом.
        DecodedJWT jwt = verifier.verify(token);

        // С декодированного JWT получаем данные.
        // Получаем таким образом любые данные.
        return jwt.getClaim("username").asString();
    }
}
