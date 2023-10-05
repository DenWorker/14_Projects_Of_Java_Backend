package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO {


    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String fullName;

    @Min(value = 1920, message = "Год рождения не может быть меньше 1920 года!")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023 года!")
    private int yearOfBorn;

    private List<BookDTO> books;


    public PersonDTO() {
    }

    public PersonDTO(String fullName, int yearOfBorn, List<BookDTO> books) {
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

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    @JsonProperty("books")
    public List<String> getBookTitles() {
        List<String> bookIds = new ArrayList<>();
        for (BookDTO book : books) {
            bookIds.add(book.getTitle());
        }
        return bookIds;
    }
}