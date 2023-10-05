package ru.DenWorker.FirstRestApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FirstRestApplication {

    //http://localhost:8080/people
    //http://localhost:8080/people/7
    // POST запрос делается в специальной проге (Postman).
    //http://localhost:8080/people - пост запрос.
    public static void main(String[] args) {
        SpringApplication.run(FirstRestApplication.class, args);
        System.out.println("Hello world!");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
