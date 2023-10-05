package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String fullName;

    @Column(name = "year_of_born")
    @Min(value = 1920, message = "Год рождения не может быть меньше 1920 года!")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023 года!")
    private int yearOfBorn;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;


    public Person() {
    }

    public Person(int person_id, String fullName, int yearOfBorn, List<Book> books) {
        this.id = person_id;
        this.fullName = fullName;
        this.yearOfBorn = yearOfBorn;
        this.books = books;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBorn() {
        return yearOfBorn;
    }

    public void setYearOfBorn(int yearOfBorn) {
        this.yearOfBorn = yearOfBorn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
