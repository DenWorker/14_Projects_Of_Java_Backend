package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Person;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.PeopleService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.PersonValidator;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final AdminDetailsService adminDetailsService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService, AdminDetailsService adminDetailsService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.adminDetailsService = adminDetailsService;
    }

    @ModelAttribute("currentAdmin")
    public AdminDetailsService.AdminDetails getCurrentUser() {
        return adminDetailsService.getAuthenticatedAdmin();
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.index());
        return "/people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.show(id).orElse(null));
        model.addAttribute("person_books", peopleService.getAllBooksOfPerson(id));
        return "/people/show";
    }

    ///////////////////////////////
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    ///////////////////////////////
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.show(id).orElse(null));
        return "/people/edit";
    }

    @PatchMapping("/{id}")
    public String patch(@ModelAttribute("person") @Valid Person person,
                        BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/people/edit";
        }
        peopleService.update(person, id);
        return "redirect:/people";
    }

    ///////////////////////////////
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    ///////////////////////////////
    @GetMapping("/search")
    public String searchPage() {
        return "/people/search";
    }

    @PostMapping("/search")
    public String MakeSearch(@RequestParam(value = "findHuman", required = false) String findHuman, Model model) {
        model.addAttribute("findHumans", peopleService.findHuman(findHuman));

        return "/people/search";
    }

}
