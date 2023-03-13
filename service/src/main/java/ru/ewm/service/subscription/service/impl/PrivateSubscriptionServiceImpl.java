package ru.ewm.service.subscription.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.SubscriptionState;
import ru.ewm.service.error.ForbiddenException;
import ru.ewm.service.subscription.dto.SubscriptionDto;
import ru.ewm.service.subscription.model.Subscription;
import ru.ewm.service.subscription.repository.SubscriptionRepository;
import ru.ewm.service.subscription.service.PrivateSubscriptionService;
import ru.ewm.service.user.model.User;
import ru.ewm.service.validation.EntityFoundValidator;

import java.time.LocalDateTime;

import static ru.ewm.service.subscription.mapper.SubscriptionMapper.toSubscriptionDto;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateSubscriptionServiceImpl implements PrivateSubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final EntityFoundValidator entityFoundValidator;

    @Override
    public SubscriptionDto addSubscription(Long userToFollowId, Long followerId) {
        User userToFollow = entityFoundValidator.checkIfUserExist(userToFollowId);
        User follower = entityFoundValidator.checkIfUserExist(followerId);

        Subscription subscriptionToSave = new Subscription();
        subscriptionToSave.setCreated(LocalDateTime.now());
        subscriptionToSave.setUser(userToFollow);
        subscriptionToSave.setFollower(follower);
        subscriptionToSave.setStatus(SubscriptionState.CONFIRMED);

        return toSubscriptionDto(subscriptionRepository.save(subscriptionToSave));
    }

    @Override
    public void deleteSubscription(Long followerId, Long subId) {
        User follower = entityFoundValidator.checkIfUserExist(followerId);
        Subscription subscriptionToCancel = entityFoundValidator.checkIfSubscriptionExist(subId);

        if (!subscriptionToCancel.getFollower().equals(follower)) {
            throw new ForbiddenException("subscription could be cancelled only by follower");
        }
        subscriptionRepository.deleteById(subId);
    }
}

