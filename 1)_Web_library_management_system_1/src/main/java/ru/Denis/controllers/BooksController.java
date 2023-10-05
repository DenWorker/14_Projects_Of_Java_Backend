package ru.Denis.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Denis.dao.BookDAO;
import ru.Denis.dao.PersonDAO;
import ru.Denis.models.Book;
import ru.Denis.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;


    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    ///////////////////////////////
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.show(id));


        Optional<Person> personOfBook = bookDAO.showPersonOfBook(id);
        if (personOfBook.isPresent()) {
            model.addAttribute("personOfBook", personOfBook.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }
        return "books/show";
    }

    ///////////////////////////////
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDAO.save(book);
        return "redirect:/books";
    }

    ///////////////////////////////
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute(bookDAO.show(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String patch(@ModelAttribute("book") @Valid Book book,
                        BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookDAO.update(book, id);
        return "redirect:/books";
    }

    ///////////////////////////////
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    ///////////////////////////////
    @PatchMapping("/{id}/free")
    public String toFree(@PathVariable("id") int id) {
        bookDAO.toFree(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assignBook")
    public String assignBook(@PathVariable("id") int book_id, @ModelAttribute("person") Person person) {
        bookDAO.assignBook(book_id, person.getId());
        return "redirect:/books/" + book_id;
    }

}
