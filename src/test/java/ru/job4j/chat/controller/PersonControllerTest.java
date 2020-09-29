package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.RunApplication;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.PersonService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RunApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void whenFindAllPersonThenReturnJsonAllPerson() throws Exception {
        Person pFirst = Person.of(1);
        Person pSecond = Person.of(2);
        List<Person> list = List.of(pFirst, pSecond);
        String rslJson = new ObjectMapper().writeValueAsString(list);
        when(service.findAll()).thenReturn(list);
        mockMvc.perform(get("/person").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenFindPersonByIdThenStatusIsOkAndReturnOneJsonPerson() throws Exception {
        int id = 1;
        Person person = Person.of(id);
        String rslJson = new ObjectMapper().writeValueAsString(person);
        when(service.findById(person)).thenReturn(Optional.of(person));
        mockMvc.perform(get("/person/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenFindPersonByIdThenStatusIsNotFoundAndReturnEmptyPerson() throws Exception {
        int id = 1;
        String rslJson = new ObjectMapper().writeValueAsString(new Person());
        when(service.findById(Person.of(id))).thenReturn(Optional.empty());
        mockMvc.perform(get("/person/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenCreatePersonThenStatusIsCreatedAndReturnJsonPerson() throws Exception {
        Person newPerson = new Person();
        newPerson.setName("root");
        String reqJson = new ObjectMapper().writeValueAsString(newPerson);
        Person createPerson = new Person();
        createPerson.setId(1);
        createPerson.setName(newPerson.getName());
        String rslJson = new ObjectMapper().writeValueAsString(createPerson);
        when(service.save(newPerson)).thenReturn(createPerson);
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenUpdatePersonThenStatusIsOk() throws Exception {
        Person person = Person.of(1);
        String reqJson = new ObjectMapper().writeValueAsString(person);
        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenDeletePersonThenStatusIsOk() throws Exception {
        mockMvc.perform(delete("/person/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void whenSignUpThenStatusOk() throws Exception {
        Person person = new Person();
        person.setUsername("root");
        person.setPassword("root");
        person.setRole(new Role());
        String reqJson = new ObjectMapper().writeValueAsString(person);
        mockMvc.perform(post("/person/sign-up").contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}