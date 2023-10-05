package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Admin;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.repositories.AdminsRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AdminDetailsService implements UserDetailsService {
    private final AdminsRepository adminsRepository;

    @Autowired
    public AdminDetailsService(AdminsRepository adminsRepository) {
        this.adminsRepository = adminsRepository;
    }

    public AdminDetails getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AdminDetails) {
            return (AdminDetails) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminsRepository.findByUsername(username);

        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Hibernate.initialize(admin.get().getRoles());

        return new AdminDetails(admin.get());
    }

    public void save(Admin newAdmin) {
        adminsRepository.save(newAdmin);
    }

    public List<Admin> index() {
        return adminsRepository.findAll();
    }

    public Optional<Admin> findById(int id) {
        return adminsRepository.findById(id);
    }

    public void updateAdmin(Admin updateAdmin, int id) {
        Optional<Admin> adminOptional = adminsRepository.findById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            admin.setUsername(updateAdmin.getUsername());
            admin.setYearOfBorn(updateAdmin.getYearOfBorn());
            admin.setRoles(updateAdmin.getRoles());

            adminsRepository.save(admin);
        }
    }

    public void deleteById(int id) {
        adminsRepository.deleteById(id);
    }


    public static final class AdminDetails extends Admin implements UserDetails {

        public AdminDetails(Admin admin) {
            super(admin.getId(), admin.getUsername(), admin.getYearOfBorn(), admin.getPassword(), admin.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.getRoles();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}