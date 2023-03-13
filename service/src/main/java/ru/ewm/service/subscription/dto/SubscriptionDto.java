package ru.ewm.service.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ewm.service.constants.SubscriptionState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long id;
    private LocalDateTime created;
    private Long user;
    private Long follower;
    private SubscriptionState status;

    @Override
    public String toString() {
        return "SubscriptionDto{" +
                "id=" + id +
                ", created=" + created +
                ", user=" + user +
                ", follower=" + follower +
                ", status=" + status +
                '}';
    }
}
