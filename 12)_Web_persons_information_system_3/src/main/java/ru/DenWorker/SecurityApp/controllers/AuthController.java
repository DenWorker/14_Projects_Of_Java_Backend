package ru.DenWorker.SecurityApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.DenWorker.SecurityApp.dto.AuthenticationDTO;
import ru.DenWorker.SecurityApp.dto.PersonDTO;
import ru.DenWorker.SecurityApp.models.Person;
import ru.DenWorker.SecurityApp.security.JWTUtil;
import ru.DenWorker.SecurityApp.services.RegistrationService;
import ru.DenWorker.SecurityApp.util.PersonValidator;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    // Внедряем наш класс, который работает с JWT.
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    ///////////////////////////////////////////////////////////

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {

        Person person = convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            // По-хорошему лучше здесь выкинуть исключение.
            return Map.of("message", "Ошибка!");
        }

        // Сохраняем челика в БД.
        registrationService.register(person);

        // Генерируем сам токен по имени челика.
        // Имя пользователя будет зашито в токен.
        // Потом имя можно будет извлечь.
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token", token);
    }

    // Так как токен будет жить 1 час, необходим метод генерации нового токена.
    // По этому адресу отправляем наш логин и пароль, и получаем новый токен.
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        // Приходит нам DTO, и его кладём в токен аутентификации.
        // Стандартный класс для инкапсуляции логина и пароля в Spring Security.
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            // Проверяем, что пароль и логин правильные.
            // Раньше была страничка, но сейчас есть специальный класс.
            // Используется токен.
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        // Если аутентификация прошла успешно, генерируем новый токен и возвращаем его.
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    ///////////////////////////////////////////////////////////

    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }
}
