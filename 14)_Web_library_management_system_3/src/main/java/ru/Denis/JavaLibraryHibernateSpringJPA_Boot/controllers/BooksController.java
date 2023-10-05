package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Book;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Person;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.BooksService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.PeopleService;

import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final AdminDetailsService adminDetailsService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService, AdminDetailsService adminDetailsService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.adminDetailsService = adminDetailsService;
    }

    @ModelAttribute("currentAdmin")
    public AdminDetailsService.AdminDetails getCurrentUser() {
        return adminDetailsService.getAuthenticatedAdmin();
    }

    ///////////////////////////////
    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear) {

        if (sortByYear == null) {
            sortByYear = false;
        }
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.index(sortByYear));
        } else {
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
        }

        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.show(id).orElse(null));


        Optional<Person> personOfBook = booksService.showOwnerOfBook(id);
        if (personOfBook.isPresent()) {
            model.addAttribute("personOfBook", personOfBook.get());
        } else {
            model.addAttribute("people", peopleService.index());
        }

        // Костыль для загрузки модели на navbar.html (шаблон, который юзается множеством страниц).
        // Без возврата этой странички модель туда не загрузить и там её не использовать.
        if (id == -3000) {
            return "/navbar";
        }
        return "/books/show";
    }

    ///////////////////////////////
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    ///////////////////////////////
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        booksService.show(id).ifPresent(book -> model.addAttribute("book", book));
        return "/books/edit";
    }


    @PatchMapping("/{id}")
    public String patch(@ModelAttribute("book") @Valid Book book,
                        BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }

        booksService.update(book, id);
        return "redirect:/books";
    }

    ///////////////////////////////
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    ///////////////////////////////
    @PatchMapping("/{id}/free")
    public String toFree(@PathVariable("id") int id) {
        booksService.toFree(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assignBook")
    public String assignBook(@PathVariable("id") int book_id, @ModelAttribute("person") Person person) {
        booksService.assignBook(book_id, person);
        return "redirect:/books/" + book_id;
    }

    ///////////////////////////////
    @GetMapping("/search")
    public String searchPage() {
        return "/books/search";
    }

    @PostMapping("/search")
    public String MakeSearch(@RequestParam(value = "findBook", required = false) String findBook, Model model) {
        model.addAttribute("findBooks", booksService.findBook(findBook));

        return "/books/search";
    }

}
