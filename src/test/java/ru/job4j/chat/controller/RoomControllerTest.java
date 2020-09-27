package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.chat.RunApplication;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RunApplication.class)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @MockBean
    private RoomService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAllRoomsThenStatusOkAndReturnJsonAllRooms() throws Exception {
        Room rFirst = Room.of(1);
        Room rSecond = Room.of(2);
        List<Room> rooms = List.of(rFirst, rSecond);
        String rslJson = new ObjectMapper().writeValueAsString(rooms);
        when(service.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/room").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenFindRoomByIdThenStatusOkAndReturnJsonOneRoom() throws Exception {
        int id = 1;
        Room room = Room.of(id);
        String rslJson = new ObjectMapper().writeValueAsString(room);
        when(service.findById(room)).thenReturn(Optional.of(room));
        mockMvc.perform(get("/room/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenFindRoomByIdThenStatusNotFoundAndReturnJsonEmptyRoom() throws Exception {
        int id = 1;
        String rslJson = new ObjectMapper().writeValueAsString(new Room());
        when(service.findById(Room.of(id))).thenReturn(Optional.empty());
        mockMvc.perform(get("/room/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenCreateRoomThenStatusIsCreatedAndReturnJsonRoom() throws Exception {
        Room newRoom = new Room();
        newRoom.setName("world");
        String reqJson = new ObjectMapper().writeValueAsString(newRoom);
        Room crtRoom = new Room();
        crtRoom.setName(newRoom.getName());
        crtRoom.setId(1);
        String rslJson = new ObjectMapper().writeValueAsString(crtRoom);
        when(service.save(newRoom)).thenReturn(crtRoom);
        mockMvc.perform(post("/room").contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(rslJson));
    }

    @Test
    public void whenUpdateRoomThenStatusIsOk() throws Exception {
        Room room = Room.of(1);
        String reqJson = new ObjectMapper().writeValueAsString(room);
        mockMvc.perform(put("/room").contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void whenDeleteRoomThenStatusIsOk() throws Exception {
        mockMvc.perform(delete("/room/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}