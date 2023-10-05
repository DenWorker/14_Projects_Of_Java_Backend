package ru.DenWorker.FirstRestApp.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "user_name")
    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters!")
    private String username;

    @Column(name = "year_of_born")
    @Min(value = 0, message = "Age should be greater than 0!")
    private int year_of_born;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty!")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "update_at")
    private LocalDateTime update_at;

    @Column(name = "created_who")
    @NotEmpty
    private String created_who;

    public Person() {
    }

    public Person(String username, int year_of_born) {
        this.username = username;
        this.year_of_born = year_of_born;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user_name) {
        this.username = user_name;
    }

    public int getYear_of_born() {
        return year_of_born;
    }

    public void setYear_of_born(int year_of_born) {
        this.year_of_born = year_of_born;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(LocalDateTime update_at) {
        this.update_at = update_at;
    }

    public String getCreated_who() {
        return created_who;
    }

    public void setCreated_who(String created_who) {
        this.created_who = created_who;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", user_name='" + username + '\'' +
                ", year_of_born=" + year_of_born +
                ", password='" + password + '\'' +
                '}';
    }
}
