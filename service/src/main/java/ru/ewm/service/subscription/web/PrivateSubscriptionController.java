package ru.ewm.service.subscription.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.subscription.service.PrivateSubscriptionService;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PrivateSubscriptionController {
    private final PrivateSubscriptionService subscriptionService;

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

}
