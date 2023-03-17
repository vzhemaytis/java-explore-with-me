package ru.ewm.service.subscription.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.event.service.PrivateEventService;
import ru.ewm.service.subscription.service.PrivateSubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PrivateSubscriptionController {
    private final PrivateSubscriptionService subscriptionService;
    private final PrivateEventService privateEventService;

    @PostMapping("/subscriptions")
    public ResponseEntity<Object> addSubscription(@PathVariable(name = "userId") Long followerId,
                                                  @RequestParam(name = "userToFollow") Long userToFollowId) {
        log.info("add new subscription on user with id = {} by user with id = {}", userToFollowId, followerId);
        return new ResponseEntity<>(subscriptionService
                .addSubscription(userToFollowId, followerId), HttpStatus.CREATED);
    }

    @DeleteMapping("/subscriptions/{subId}")
    public ResponseEntity<Object> deleteSubscription(@PathVariable(name = "userId") Long followerId,
                                                     @PathVariable(name = "subId") Long subId) {
        log.info("delete subscription with id = {} by user with id = {}", subId, followerId);
        subscriptionService.deleteSubscription(followerId, subId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/events/subscriptions")
    public ResponseEntity<Object> getAllFollowedUsersEvents(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size,
            @RequestParam(name = "sort", required = false) SortTypes sort) {
        log.info("get all followed users events by user with id = {}", userId);
        return new ResponseEntity<>(privateEventService
                .getAllFollowedUsersEvents(userId, from, size, sort), HttpStatus.OK);
    }
}
