package ru.ewm.service.event.repository.impl;

import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findEvents(List<Long> users,
                                  List<EventState> states,
                                  List<Long> categories,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  long from,
                                  int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        Predicate criteria = cb.conjunction();

        if (users != null) {
            Predicate inUsers = event.get("initiator").in(users);
            criteria = cb.and(criteria, inUsers);
        }

        if (states != null) {
            Predicate inStates = event.get("state").in(states);
            criteria = cb.and(criteria, inStates);
        }

        if (categories != null) {
            Predicate inCategories = event.get("category").in(categories);
            criteria = cb.and(criteria, inCategories);
        }

        if (rangeStart != null) {
            Predicate start = cb.greaterThan(event.get("eventDate"), rangeStart);
            criteria = cb.and(criteria, start);
        }

        if (rangeEnd != null) {
            Predicate end = cb.lessThan(event.get("eventDate"), rangeEnd);
            criteria = cb.and(criteria, end);
        }

        Predicate idFrom = cb.greaterThanOrEqualTo(event.get("id"), from);
        criteria = cb.and(criteria, idFrom);
        query.select(event).where(criteria);
        return entityManager.createQuery(query).setMaxResults(size).getResultList();
    }
}
