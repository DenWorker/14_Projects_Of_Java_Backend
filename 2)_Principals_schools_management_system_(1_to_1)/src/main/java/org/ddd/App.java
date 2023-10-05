package org.ddd;

import Models.Principal;
import Models.School;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class App {
    static Configuration configuration = new Configuration()
            .addAnnotatedClass(Principal.class)
            .addAnnotatedClass(School.class);

    static SessionFactory sessionFactory = configuration.buildSessionFactory();
    static Session session = sessionFactory.getCurrentSession();

    public static void main(String[] args) {
        try {
            session.beginTransaction();

            getPrincipalAndHisSchool(1);
            getSchoolAndPrincipal(1);

            Principal principal = newPrincipal("Spider-man", 25);
            School school = newSchool(333, principal);

            editPrincipalOfSchool(1, 1);

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }

    public static void getPrincipalAndHisSchool(int idOfPrincipal) {
        Principal principal = session.get(Principal.class, idOfPrincipal);
        System.out.println("Информация о директоре:");
        System.out.println(principal.toString());
        System.out.println("Информация о его школе:");
        System.out.println(principal.getSchool().toString());
        System.out.println("-------------------------------------");
    }

    public static void getSchoolAndPrincipal(int idOfSchool) {
        School school = session.get(School.class, idOfSchool);
        System.out.println("Информация о школе:");
        System.out.println(school.toString());
        System.out.println("Информация о директоре этой школы:");
        System.out.println(school.getPrincipal().toString());
        System.out.println("-------------------------------------");
    }

    public static Principal newPrincipal(String name, int age) {
        Principal principal = new Principal(name, age);
        session.persist(principal);
        return principal;
    }

    public static School newSchool(int schoolNumber, Principal principal) {
        School school = new School(schoolNumber, principal);
        session.persist(school);
        return school;
    }

    public static void editPrincipalOfSchool(int idOfSchool, int idOfNewPrincipal) {
        School school = session.get(School.class, idOfSchool);
        school.setPrincipal(session.get(Principal.class, idOfNewPrincipal));
    }
}

