package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<Admin> admins;


    public Role(String roleName, List<Admin> admins) {
        this.roleName = roleName;
        this.admins = admins;
    }

    public Role() {
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role) {
        this.roleName = role;
    }

    public int getId() {
        return id;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

}
