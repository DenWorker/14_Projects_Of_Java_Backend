package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @Size(min = 2, max = 40, message = "Размер названия книги: от 2 до 30!")
    private String title;

    @Column(name = "author")
    @Size(min = 2, max = 40, message = "Размер ФИО автора: от 2 до 30!")
    private String author;

    @Column(name = "release_date")
    @Max(value = 2023, message = "Год издания не может быть больше 2023 года!")
    private int releaseDate;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taking_book")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takingBook;

    public Book() {
    }

    public Book(int id, String title, String author, int releaseDate, Person owner, Date takingBook) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.owner = owner;
        this.takingBook = takingBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakingBook() {
        return takingBook;
    }

    public void setTakingBook(Date takingBook) {
        this.takingBook = takingBook;
    }

    public boolean isDelay() {
        return (new Date().getTime() - takingBook.getTime() > 864000000);
    }

}