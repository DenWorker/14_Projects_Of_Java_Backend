package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String username;

    @Column(name = "yearofborn")
    @Min(value = 1920, message = "Год рождения не может быть меньше 1920 года!")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023 года!")
    private int yearOfBorn;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "admins_roles",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;


    public Admin(int id, String username, int yearOfBorn, String password, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.yearOfBorn = yearOfBorn;
        this.password = password;
        this.roles = roles;
    }

    public Admin(String username, int yearOfBorn, String password) {
        this.username = username;
        this.yearOfBorn = yearOfBorn;
        this.password = password;
    }

    public Admin() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBorn() {
        return yearOfBorn;
    }

    public void setYearOfBorn(int yearOfBorn) {
        this.yearOfBorn = yearOfBorn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getStringRoles() {
        return getRoles().stream().map(Role::getRoleName).collect(Collectors.joining(";  "));
    }

}