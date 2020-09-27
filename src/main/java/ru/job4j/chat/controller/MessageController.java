package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/room/{id_room}/message")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Message>> findAll(@PathVariable("id_room") Integer idRoom) {
        var messages = service.findByRoom(Room.of(idRoom));
        return new ResponseEntity<>(
                messages.orElse(List.of()),
                messages.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Message> create(@PathVariable("id_room") Integer idRoom,
                                          @RequestBody Message message) {
        message.setRoom(Room.of(idRoom));
        return new ResponseEntity<>(service.save(message), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> update(@PathVariable("id_room") Integer idRoom,
                                       @RequestBody Message message) {
        message.setRoom(Room.of(idRoom));
        var msg = service.findById(message);
        Person updPerson = message.getPerson();
        Person crtPerson = msg.get().getPerson();
        if (updPerson.getId().equals(crtPerson.getId())) {
            service.save(message);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id_message}")
    public ResponseEntity<Void> delete(@PathVariable("id_message") Integer idMessage) {
        service.delete(Message.of(idMessage));
        return ResponseEntity.ok().build();
    }
}
