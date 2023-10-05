package ru.DenWorker.SecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.DenWorker.SecurityApp.models.PersonDetails;

@Controller
public class HelloController {

    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo() {
        // Получаем данные из контекста.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Возвращено будет то, что получено из самого JWT токена.
        return personDetails.getUsername();
    }
}