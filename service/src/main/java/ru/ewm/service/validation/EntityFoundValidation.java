package ru.ewm.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.category.repository.CategoryRepository;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.user.model.User;
import ru.ewm.service.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityFoundValidation {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public User checkIfUserExist(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(userId, User.class.getSimpleName());
        }
        return user.get();
    }

    public Event checkIfEventExist(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EntityNotFoundException(eventId, Event.class.getSimpleName());
        }
        return event.get();
    }

    public Category checkIfCategoryExist(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            throw new EntityNotFoundException(catId, Category.class.getSimpleName());
        }
        return category.get();
    }
}
