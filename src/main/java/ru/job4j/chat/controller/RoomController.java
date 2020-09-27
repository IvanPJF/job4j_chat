package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.RoomService;

import java.util.Collection;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<Room>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable Integer id) {
        var room = service.findById(Room.of(id));
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(service.save(room), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Room room) {
        service.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(Room.of(id));
        return ResponseEntity.ok().build();
    }
}
