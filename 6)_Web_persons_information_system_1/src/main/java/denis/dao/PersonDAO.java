package denis.dao;


import denis.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
public class PersonDAO {

    // Внедряем sessionFactory.
    // Теперь можем работать с Hibernate.
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Эта аннотация открывает и закрывает транзакцию.
    // readOnly = true - не вносим изменения в БД (небольшое ускорение).
    @Transactional(readOnly = true)
    public List<Person> index() {
        return sessionFactory.getCurrentSession().createQuery("SELECT p FROM Person p", Person.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Person> show(String email) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person p WHERE p.email = :emailParam", Person.class)
                .setParameter("emailParam", email).stream().findAny();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        Session session = sessionFactory.getCurrentSession();
        Person person1 = session.get(Person.class, id);

        person1.setName(person.getName());
        person1.setAge(person.getAge());
        person1.setEmail(person.getEmail());
        person1.setAddress(person.getAddress());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Person.class, id));
    }

}
