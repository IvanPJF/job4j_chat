package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;

import java.util.Collection;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    Optional<Collection<Message>> findByRoom(Room room);
}
