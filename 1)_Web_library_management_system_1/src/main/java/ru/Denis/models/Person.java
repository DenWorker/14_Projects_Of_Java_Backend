package ru.Denis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Person {

    @Size(min = 2, max = 40, message = "Размер ФИО: от 2 до 30!")
    private String fullName;
    @Min(value = 1920, message = "Год рождения не может быть меньше 1920 года!")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023 года!")
    private int yearOfBorn;
    private int id;

    public Person() {
    }

    public Person(String fullName, int yearOfBorn, int id) {
        this.fullName = fullName;
        this.yearOfBorn = yearOfBorn;
        this.id = id;
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
}
