package ru.ewm.service.subscription.service;

import ru.ewm.service.subscription.dto.SubscriptionDto;

public interface PrivateSubscriptionService {
    SubscriptionDto addSubscription(Long userToFollowId, Long followerId);

    void deleteSubscription(Long followerId, Long subId);
}
