package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Admin;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.RolesService;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RolesService rolesService;
    private final AdminDetailsService adminDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(RolesService rolesService, AdminDetailsService adminDetailsService, PasswordEncoder passwordEncoder) {
        this.rolesService = rolesService;
        this.adminDetailsService = adminDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    String login() {
        return "/auth/login";
    }

    @GetMapping("/error")
    String failLogin() {
        return "/auth/errorPage";
    }


    @GetMapping("/registration")
    String registration(@ModelAttribute("admin") Admin admin, Model model) {
        model.addAttribute("roles", rolesService.allRoles());
        return "/auth/registration";
    }

    @PostMapping("/registration")
    String registration(@ModelAttribute("admin") @Valid Admin newAdmin,
                        BindingResult bindingResult,
                        Model model,
                        @RequestParam(name = "roles", required = false) List<Integer> roleIds) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolesService.allRoles());
            return "/auth/registration";
        }

        if (roleIds != null) {
            List<Role> selectedRoles = rolesService.getRolesByIds(roleIds);
            newAdmin.setRoles(selectedRoles);
        }

        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        adminDetailsService.save(newAdmin);

        return "redirect:/auth/login";
    }
}
