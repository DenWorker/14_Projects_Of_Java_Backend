package ru.Denis.JavaLibraryHibernateSpringJPA_Boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.config.StaticResourceConfiguration;

// Check git.
@SpringBootApplication
@Import(StaticResourceConfiguration.class)
public class Web_library_management_system_3 {

    //http://localhost:8080/books
    public static void main(String[] args) {
        SpringApplication.run(Web_library_management_system_3.class, args);
    }

}
