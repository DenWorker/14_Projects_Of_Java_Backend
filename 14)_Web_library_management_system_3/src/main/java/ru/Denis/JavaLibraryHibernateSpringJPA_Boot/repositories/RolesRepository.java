package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String role);

    List<Role> findByIdIn(List<Integer> roleIds);
}
