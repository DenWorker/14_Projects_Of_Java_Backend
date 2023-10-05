package org.DDD;

import Models.Director;
import Models.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Collections;


public class App {
    public static Configuration configuration = new Configuration().addAnnotatedClass(Director.class).addAnnotatedClass(Film.class);
    public static SessionFactory sessionFactory = configuration.buildSessionFactory();
    public static Session session = sessionFactory.getCurrentSession();

    public static void main(String[] args) {
        try {
            session.beginTransaction();


            // Вставляем методы. Не забываем про то, как работают сессии.
            // После всех изменений необходим session.getTransaction().commit().


            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }

    public static void printDirectorsInformation(int id) {
        System.out.println("Информация о режиссёре с id: " + id);
        System.out.println(session.get(Director.class, id).toString());
        System.out.println("Информация о фильмах этого режиссёра:");
        session.get(Director.class, id).getListOfFilms().forEach(t -> System.out.println(t.toString()));
        System.out.println("-----------------------------------------");
    }

    public static void printFilmsInformation(int id) {
        System.out.println("Информация о фильме с id: " + id);
        System.out.println(session.get(Film.class, id).toString());
        System.out.println("Информация о режиссёре этого фильма:");
        System.out.println(session.get(Film.class, id).getDirector().toString());
        System.out.println("-----------------------------------------");
    }

    public static void addNewFilm(String nameOfFilm, int yearOfProductionsFilm, int idOfDirector) {
        Director director = session.get(Director.class, idOfDirector);
        Film film = new Film(nameOfFilm, yearOfProductionsFilm, director);

        director.getListOfFilms().add(film);

        session.persist(film);
        session.persist(director);
    }

    public static void addNewDirector(String nameOfDirector, int ageOfDirector) {
        Director director = new Director(nameOfDirector, ageOfDirector);


        director.setListOfFilms(new ArrayList<>(Collections.singletonList(null)));
        session.persist(director);
    }

    public static void editDirectorOfFilm(int idOfFilm, int idOfNewDirector) {
        Film film = session.get(Film.class, idOfFilm);
        Director director = session.get(Director.class, idOfNewDirector);

        film.getDirector().getListOfFilms().remove(film);
        director.getListOfFilms().add(film);
        film.setDirector(director);
    }

    public static void deleteFilmOfDirector(int idOfFilm) {
        Film film = session.get(Film.class, idOfFilm);

        film.getDirector().getListOfFilms().remove(film);
        session.remove(film);
    }
}
