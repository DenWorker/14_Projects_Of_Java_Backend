package ru.Denis.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Denis.models.Book;
import ru.Denis.models.Person;
import ru.Denis.repositories.PeopleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    public Optional<Person> show(int id) {
        return peopleRepository.findById(id);
    }

    public List<Book> getAllBooksOfPerson(int id) {
        if (peopleRepository.findById(id).isPresent()) {
            Hibernate.initialize(peopleRepository.findById(id).get().getBooks());
            return peopleRepository.findById(id).get().getBooks();
        } else return Collections.emptyList();
    }

    @Transactional
    public void save(Person newPerson) {
        peopleRepository.save(newPerson);
    }

    @Transactional
    public void update(Person updatePerson, int id) {
        updatePerson.setId(id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullNameOfPerson) {
        return peopleRepository.findPersonByFullName(fullNameOfPerson);
    }
}
