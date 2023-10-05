package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllersREST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto.BookDTO;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Book;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.BooksService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booksREST")
public class BooksControllerREST {
    private final BooksService booksService;
    private final ModelMapper modelMapper;

    @Autowired
    public BooksControllerREST(BooksService booksService, ModelMapper modelMapper) {
        this.booksService = booksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/getAllBooks")
    public List<BookDTO> getBooksDTO() {
        return booksService.index(true).stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/getBookById/{id}")
    public BookDTO getBookDTOById(@PathVariable("id") int id) {
        return convertToBookDTO(booksService.show(id).orElse(null));
    }

    @PostMapping("/saveNewBook")
    public ResponseEntity<HttpStatus> saveNewBook(@RequestBody BookDTO bookDTO) {
        booksService.save(convertToBook(bookDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> patch(@RequestBody BookDTO bookDTO,
                                            @PathVariable("id") int id) {

        booksService.update(convertToBook(bookDTO), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/findBook")
    public List<BookDTO> findPerson(@RequestBody String title) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(title);
            if (jsonNode.size() > 0) {
                return booksService.findBook(jsonNode.get("title").asText())
                        .stream()
                        .map(this::convertToBookDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @PatchMapping("/free/{id}")
    public ResponseEntity<HttpStatus> toFree(@PathVariable("id") int id) {
        booksService.toFree(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/assignBook/{id}")
    public ResponseEntity<HttpStatus> assignBook(@PathVariable("id") int book_id, @RequestBody String person_id) {
        try {
            booksService.assignBookById(book_id, new ObjectMapper().readTree(person_id).get("person_id").asInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

}