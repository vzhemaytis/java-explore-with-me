package ru.ewm.service.subscription.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.SubscriptionState;
import ru.ewm.service.subscription.model.Subscription;
import ru.ewm.service.subscription.repository.SubscriptionRepository;
import ru.ewm.service.subscription.service.CommonSubscriptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonSubscriptionServiceImpl implements CommonSubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> getAllActiveSubscriptionsByFollowerId(Long followerId) {
        return subscriptionRepository.findAllByFollowerIdAndStatusIs(followerId, SubscriptionState.CONFIRMED);
    }
}
