package ru.ivanov.denis.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.denis.models.Person;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PersonDAO {

    private final EntityManager entityManager;

    @Autowired
    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void testNPlus1() {
        try (Session session = entityManager.unwrap(Session.class)) {
            Stream<Person> personStream = session.createQuery(
                            "select p from Person p LEFT JOIN FETCH p.items",
                            Person.class)
                    .getResultStream();

            List<Person> people = personStream.collect(Collectors.toList());

            for (Person iterator : people) {
                System.out.println(iterator.getName());
                System.out.println("Товары человека:");
                System.out.println(iterator.getItems());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
