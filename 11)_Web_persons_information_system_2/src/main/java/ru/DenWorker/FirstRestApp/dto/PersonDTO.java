package ru.DenWorker.FirstRestApp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters!")
    private String username;

    @Min(value = 0, message = "Age should be greater than 0!")
    private int year_of_born;

    @NotEmpty(message = "Password should not be empty!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
