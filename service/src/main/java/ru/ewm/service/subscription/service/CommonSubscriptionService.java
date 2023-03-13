package ru.ewm.service.subscription.service;

import ru.ewm.service.subscription.model.Subscription;

import java.util.List;

public interface CommonSubscriptionService {
    List<Subscription> getAllActiveSubscriptionsByFollowerId(Long followerId);
}
