package ru.ewm.service.subscription.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.ewm.service.subscription.dto.SubscriptionDto;
import ru.ewm.service.subscription.model.Subscription;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {
    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getId(),
                subscription.getCreated(),
                subscription.getUser().getId(),
                subscription.getFollower().getId(),
                subscription.getStatus()
        );
    }
}
