package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto;

import jakarta.validation.constraints.Size;


public class AuthAdminDTO {

    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

