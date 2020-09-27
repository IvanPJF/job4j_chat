package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Optional<Collection<Message>> findByRoom(Room room) {
        return repository.findByRoom(room);
    }

    public Optional<Message> findById(Message message) {
        return repository.findById(message.getId());
    }

    public Message save(Message message) {
        return repository.save(message);
    }

    public void delete(Message message) {
        repository.delete(message);
    }
}
