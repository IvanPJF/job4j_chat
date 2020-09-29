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
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RunApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTest {

    @MockBean
    private MessageService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void whenFindAllMessageInRoomNumber1ThenReturnJsonAllMessagesFromThisRoom() throws Exception {
        int idRoom = 1;
        Message mFirst = Message.of(1);
        Message mSecond = Message.of(2);
        List<Message> messages = List.of(mFirst, mSecond);
        String rslJson = new ObjectMapper().writeValueAsString(messages);
        when(service.findByRoom(Room.of(idRoom))).thenReturn(Optional.of(messages));
        mockMvc.perform(get("/room/{id_room}/message", idRoom))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenFindAllMessageInRoomNumberIncorrectThenStatusNoContentAndReturnJsonEmptyListFromThisRoom() throws Exception {
        int idRoom = 10;
        String rslJson = new ObjectMapper().writeValueAsString(List.of());
        when(service.findByRoom(Room.of(idRoom))).thenReturn(Optional.empty());
        mockMvc.perform(get("/room/{id_room}/message", idRoom))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenCreateMessageThenStatusIsCreatedAndReturnJsonMessage() throws Exception {
        int idRoom = 1;
        Message newMessage = new Message();
        newMessage.setBody("Hi, world");
        String reqJson = new ObjectMapper().writeValueAsString(newMessage);
        Message crtMessage = new Message();
        crtMessage.setBody(newMessage.getBody());
        crtMessage.setId(1);
        String rslJson = new ObjectMapper().writeValueAsString(crtMessage);
        when(service.save(newMessage)).thenReturn(crtMessage);
        mockMvc.perform(post("/room/{id_room}/message", idRoom)
                .contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(rslJson));
    }

    @Test
    @WithMockUser
    public void whenUpdateMessageThenStatusIsOk() throws Exception {
        int idRoom = 1;
        Person person = Person.of(1);
        Message message = new Message();
        message.setId(1);
        message.setPerson(person);
        String reqJson = new ObjectMapper().writeValueAsString(message);
        when(service.findById(message)).thenReturn(Optional.of(message));
        mockMvc.perform(put("/room/{id_room}/message", idRoom)
                .contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenUpdateMessageThenStatusIsBadRequest() throws Exception {
        int idRoom = 1;
        Message message = Message.of(1);
        message.setPerson(Person.of(1));
        Message updMessage = Message.of(message.getId());
        updMessage.setPerson(Person.of(222));
        String reqJson = new ObjectMapper().writeValueAsString(updMessage);
        when(service.findById(message)).thenReturn(Optional.of(message));
        mockMvc.perform(put("/room/{id_room}/message", idRoom)
                .contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser
    public void whenDeleteMessageThenStatusIsOk() throws Exception {
        mockMvc.perform(delete("/room/{id_room}/message/{id}", 1, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}