package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Book;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByTitleStartingWith(String title);
}
