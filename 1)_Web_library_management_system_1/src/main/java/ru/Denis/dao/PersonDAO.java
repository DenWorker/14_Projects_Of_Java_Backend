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
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM people", new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO people(fullname, year_Of_Born) VALUES(?,?)",
                person.getFullName(), person.getYearOfBorn());
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM people WHERE id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findFirst().orElse(null);
    }

    public List<Book> getPersonBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE person_id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return jdbcTemplate.query("SELECT * FROM people WHERE fullname = ?",
                new Object[]{fullName}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void update(Person person, int id) {
        jdbcTemplate.update("UPDATE people SET fullname = ?, year_of_born = ? WHERE id = ?",
                person.getFullName(), person.getYearOfBorn(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM people WHERE id = ?", id);
    }

}
