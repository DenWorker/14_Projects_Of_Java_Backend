package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDTO {

    private int id;
    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String username;

    @Min(value = 1920, message = "Год рождения не может быть меньше 1920 года!")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023 года!")
    private int yearOfBorn;
    private List<Role> roles;

    public AdminDTO() {
    }

    public AdminDTO(String username, int yearOfBorn) {
        this.username = username;
        this.yearOfBorn = yearOfBorn;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBorn() {
        return yearOfBorn;
    }

    public void setYearOfBorn(int yearOfBorn) {
        this.yearOfBorn = yearOfBorn;
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public String getStringRoles() {
        return getRoles().stream().map(Role::getRoleName).collect(Collectors.joining(";  "));
    }

    @JsonProperty("roles")
    public List<String> getRoleNames() {
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getRoleName());
        }
        return roleNames;
    }
}
