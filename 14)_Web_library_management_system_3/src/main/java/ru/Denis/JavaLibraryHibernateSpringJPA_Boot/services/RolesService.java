package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories.RolesRepository;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    @Autowired
    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> allRoles() {
        return rolesRepository.findAll();
    }

    public List<Role> getRolesByIds(List<Integer> roleIds) {
        return rolesRepository.findByIdIn(roleIds);
    }
}
