package ru.Denis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.Denis.models.Book;
import ru.Denis.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO books(title, author, release_date) VALUES (?,?,?)",
                book.getTitle(), book.getAuthor(), book.getReleaseDate());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE books.id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findFirst().orElse(null);
    }

    public Optional<Person> showPersonOfBook(int id) {
        return jdbcTemplate.query("SELECT people.* from books JOIN people on people.id = books.person_id WHERE books.id = ?;",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }


    public void update(Book book, int id) {
        jdbcTemplate.update("UPDATE books SET title = ?, author = ?, release_date = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getReleaseDate(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    public void toFree(int id) {
        jdbcTemplate.update("UPDATE books SET person_id = null WHERE id = ?", id);
    }

    public void assignBook(int book_id, int person_id) {
        jdbcTemplate.update("UPDATE books SET person_id = ? WHERE books.id = ?", person_id, book_id);
    }

}