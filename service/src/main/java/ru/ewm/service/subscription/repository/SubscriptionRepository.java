package ru.ewm.service.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.constants.SubscriptionState;
import ru.ewm.service.subscription.model.Subscription;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByFollowerIdAndStatusIs(Long followerId, SubscriptionState status);
}
