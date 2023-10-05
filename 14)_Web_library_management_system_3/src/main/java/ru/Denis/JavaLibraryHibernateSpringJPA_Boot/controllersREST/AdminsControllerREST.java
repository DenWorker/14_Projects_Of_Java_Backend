package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllersREST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto.AdminDTO;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Admin;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Role;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.AdminDetailsService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.RolesService;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.TranslationResponse;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util.Translator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/adminsREST")
public class AdminsControllerREST {

    private final AdminDetailsService adminDetailsService;
    private final ModelMapper modelMapper;
    private final Translator translator;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminsControllerREST(AdminDetailsService adminDetailsService, ModelMapper modelMapper, Translator translator, RolesService rolesService, PasswordEncoder passwordEncoder) {
        this.adminDetailsService = adminDetailsService;
        this.modelMapper = modelMapper;
        this.translator = translator;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/getAllAdmins")
    public List<AdminDTO> getAllAdmins() {
        return adminDetailsService.index().stream().map(this::convertToAdminDTO).collect(Collectors.toList());
    }

    @GetMapping("/getCurrentAdmin")
    public AdminDTO getCurrentAdmin() {
        return convertToAdminDTO(adminDetailsService.getAuthenticatedAdmin());
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<HttpStatus> patch(@PathVariable("id") int id,
                                            @RequestBody String json) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            if (json != null) {
                AdminDTO updateAdminDTO = new AdminDTO(jsonNode.get("newUsername").asText(), jsonNode.get("newYearOfBorn").asInt());
                List<Role> selectedRoles = rolesService.getRolesByIds(StreamSupport.stream(jsonNode.get("newRoleIds").spliterator(), false)
                        .map(JsonNode::asInt)
                        .collect(Collectors.toList()));
                updateAdminDTO.setRoles(selectedRoles);

                adminDetailsService.updateAdmin(convertToAdmin(updateAdminDTO), id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/newAdmin")
    public ResponseEntity<HttpStatus> newAdmin(@RequestBody String json) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            if (json != null) {
                Admin admin = new Admin(jsonNode.get("username").asText(), jsonNode.get("yearOfBorn").asInt(), passwordEncoder.encode(jsonNode.get("password").asText()));
                List<Role> selectedRoles = rolesService.getRolesByIds(StreamSupport.stream(jsonNode.get("roleIds").spliterator(), false)
                        .map(JsonNode::asInt)
                        .collect(Collectors.toList()));
                admin.setRoles(selectedRoles);
                adminDetailsService.save(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable("id") int id) {
        adminDetailsService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/translator")
    public TranslationResponse[] translate(@RequestBody String request) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(request);
            if (jsonNode.size() > 0) {
                return translator.translateREST(jsonNode.get("sentenceToTranslate").asText(), jsonNode.get("selectedLanguage").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AdminDTO convertToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }

    public AdminDTO convertToAdminDTO(AdminDetailsService.AdminDetails admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }

    public Admin convertToAdmin(AdminDTO adminDTO) {
        return modelMapper.map(adminDTO, Admin.class);
    }
}
