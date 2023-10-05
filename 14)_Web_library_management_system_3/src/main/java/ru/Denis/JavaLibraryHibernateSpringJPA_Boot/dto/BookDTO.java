package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {


    private String title;
    private String author;
    private int releaseDate;
    private PersonDTO owner;


    private Date takingBook;

    public BookDTO() {
    }

    public BookDTO(String title, String author, int releaseDate, PersonDTO owner, Date takingBook) {
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

    public PersonDTO getOwner() {
        return owner;
    }

    public void setOwner(PersonDTO owner) {
        this.owner = owner;
    }

    public Date getTakingBook() {
        return takingBook;
    }

    public void setTakingBook(Date takingBook) {
        this.takingBook = takingBook;
    }

    public Boolean isDelay() {
        if (takingBook == null) {
            return null;
        }
        return new Date().getTime() - takingBook.getTime() > 864000000;
    }

    @JsonProperty("owner")
    public String getOwnerFullName() {
        if (owner != null) {
            return owner.getFullName();
        }
        return "-";
    }

}
