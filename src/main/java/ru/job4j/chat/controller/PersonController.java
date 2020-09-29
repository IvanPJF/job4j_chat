package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.PersonService;

import java.util.Collection;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService service;
    private final BCryptPasswordEncoder encoder;

    public PersonController(PersonService service, BCryptPasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<Collection<Person>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable("id") Integer id) {
        var person = service.findById(Person.of(id));
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                service.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Person person) {
        service.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(Person.of(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        service.save(person);
        return ResponseEntity.ok().build();
    }
}
