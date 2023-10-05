package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Book;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Person;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
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

    public void save(Person newPerson) {
        peopleRepository.save(newPerson);
    }


    public void update(Person updatePerson, int id) {
        updatePerson.setId(id);
        peopleRepository.save(updatePerson);
    }


    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullNameOfPerson) {
        return peopleRepository.findPersonByFullName(fullNameOfPerson);
    }

    public List<Person> findHuman(String findHuman) {
        if (findHuman == null || findHuman.equals("")) return Collections.emptyList();
        return peopleRepository.findPersonByFullNameStartingWith(findHuman);
    }
}
