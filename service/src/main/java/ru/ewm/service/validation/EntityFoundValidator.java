package ru.ewm.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.category.repository.CategoryRepository;
import ru.ewm.service.compilation.model.Compilation;
import ru.ewm.service.compilation.repository.CompilationRepository;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.repository.RequestRepository;
import ru.ewm.service.user.model.User;
import ru.ewm.service.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityFoundValidator {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;

    public User checkIfUserExist(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new EntityNotFoundException(userId, User.class.getSimpleName()));
    }

    public Event checkIfEventExist(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        return event.orElseThrow(() -> new EntityNotFoundException(eventId, Event.class.getSimpleName()));
    }

    public Category checkIfCategoryExist(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        return category.orElseThrow(() -> new EntityNotFoundException(catId, Category.class.getSimpleName()));
    }

    public ParticipationRequest checkIfRequestExist(Long requestId) {
        Optional<ParticipationRequest> request = requestRepository.findById(requestId);
        return request
                .orElseThrow(() -> new EntityNotFoundException(requestId, ParticipationRequest.class.getSimpleName()));
    }

    public Compilation checkIfCompilationExist(Long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        return compilation.orElseThrow(() -> new EntityNotFoundException(compId, Compilation.class.getSimpleName()));
    }
}
