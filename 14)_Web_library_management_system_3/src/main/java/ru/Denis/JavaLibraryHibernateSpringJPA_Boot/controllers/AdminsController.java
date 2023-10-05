package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto.AdminDTO;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Admin;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.RolesService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.Translator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
public class AdminsController {

    private final AdminDetailsService adminDetailsService;
    private final RolesService rolesService;
    private final ModelMapper modelMapper;
    private final Translator translator;

    @Autowired
    public AdminsController(AdminDetailsService adminDetailsService, RolesService rolesService, ModelMapper modelMapper, Translator translator) {
        this.adminDetailsService = adminDetailsService;
        this.rolesService = rolesService;
        this.modelMapper = modelMapper;
        this.translator = translator;
    }

    @ModelAttribute("currentAdmin")
    public Admin getCurrentUser() {
        return adminDetailsService.getAuthenticatedAdmin();
    }

    @GetMapping("/admin")
    public String show(Model model) {
        model.addAttribute("showTranslatorLink", adminDetailsService.getAuthenticatedAdmin().getRoles().stream().anyMatch(role ->
                role.getRoleName().equals("ROLE_ADMIN_INTERPRETER") || role.getRoleName().equals("ROLE_ADMIN_MAIN")));
        return "/admins/show";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("admins", adminDetailsService.index().stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList()));
        return "/admins/index";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("updateAdmin", convertToAdminDTO(adminDetailsService.findById(id).orElse(null)));
        model.addAttribute("roles", rolesService.allRoles());
        return "/admins/edit";
    }

    @PatchMapping("/{id}/edit")
    public String patch(@ModelAttribute("updateAdmin") @Valid AdminDTO updateAdminDTO,
                        BindingResult bindingResult, @PathVariable("id") int id,
                        @RequestParam(name = "roles", required = false) List<Integer> roleIds) {

        if (bindingResult.hasErrors()) {
            return "/admins/edit";
        }

        if (roleIds != null) {
            List<Role> selectedRoles = rolesService.getRolesByIds(roleIds);
            updateAdminDTO.setRoles(selectedRoles);
        }

        adminDetailsService.updateAdmin(convertToAdmin(updateAdminDTO), id);
        return "redirect:/admins/index";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteById(@PathVariable("id") int id) {
        adminDetailsService.deleteById(id);
        return "redirect:/admins/index";
    }

    @GetMapping("/translator")
    public String showTranslator() {
        return "/admins/translatorPage";
    }

    @PostMapping("/translator")
    public String translate(@RequestParam("sentenceToTranslate") String sentenceToTranslate,
                            @RequestParam("selectedLanguage") String selectedLanguage,
                            Model model) throws IOException, InterruptedException {
        String result = translator.translate(sentenceToTranslate, selectedLanguage);
        model.addAttribute("translatedText", result);
        model.addAttribute("selectedLanguage", selectedLanguage);
        model.addAttribute("sentenceToTranslate", sentenceToTranslate);
        return "/admins/translatorPage";
    }


    private Admin convertToAdmin(AdminDTO adminDTO) {
        return modelMapper.map(adminDTO, Admin.class);
    }

    private AdminDTO convertToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }

}