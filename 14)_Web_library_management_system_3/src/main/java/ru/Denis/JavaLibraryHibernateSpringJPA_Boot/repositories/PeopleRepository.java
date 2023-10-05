package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Person;

import java.util.List;
import java.util.Optional;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByFullName(String fullNameOfPerson);

    List<Person> findPersonByFullNameStartingWith(String fullNameOfPerson);

}
