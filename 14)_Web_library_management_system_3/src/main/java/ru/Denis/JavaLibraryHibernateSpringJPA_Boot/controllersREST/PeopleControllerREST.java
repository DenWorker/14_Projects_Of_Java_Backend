package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.controllersREST;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.dto.PersonDTO;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.models.Person;
import ru.Denis.JavaLibraryHibernateSpringJPA_Boot.services.PeopleService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/peopleREST")
public class PeopleControllerREST {

    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleControllerREST(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/getAllPeople")
    public List<PersonDTO> getAllPeople() {
        return peopleService.index().stream()
                .map(this::convertToPersonDTO).collect(Collectors.toList());
    }

    @GetMapping("/getPersonById/{id}")
    public PersonDTO getPersonById(@PathVariable("id") int id) {
        return convertToPersonDTO(peopleService.show(id).orElse(null));
    }

    @PostMapping("/saveNewPerson")
    public ResponseEntity<HttpStatus> saveNewPerson(@RequestBody @Valid PersonDTO personDTO) {
        peopleService.save(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/editPerson/{id}")
    public ResponseEntity<HttpStatus> editPerson(@PathVariable("id") int id,
                                                 @RequestBody @Valid PersonDTO personDTO) {
        peopleService.update(convertToPerson(personDTO), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/deletePerson/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/findPerson")
    public List<PersonDTO> findPerson(@RequestBody String fullName) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(fullName);
            if (jsonNode.size() > 0) {
                return peopleService.findHuman(jsonNode.get("fullName").asText())
                        .stream()
                        .map(this::convertToPersonDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
