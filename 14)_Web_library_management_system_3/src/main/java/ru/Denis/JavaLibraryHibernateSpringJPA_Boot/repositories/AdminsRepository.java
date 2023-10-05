package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Admin;

import java.util.Optional;

@Repository
public interface AdminsRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String fullName);
}
