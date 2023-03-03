package ru.ewm.service.participation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.participation.service.PrivateRequestService;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @PostMapping("/requests")
    public ResponseEntity<Object> addRequest(@PathVariable Long userId,
                                     @RequestParam(name = "eventId") Long eventId) {
        log.info("add new participation request from user with id = {} to event with id = {}",
                userId, eventId);
        return new ResponseEntity<>(privateRequestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> findAllUsersRequests(@PathVariable Long userId) {
        log.info("find all participation requests from user with id = {}", userId);
        return new ResponseEntity<>(privateRequestService.findAllUsersRequests(userId), HttpStatus.OK);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        log.info("cancel participation request with id = {} bu user with id = {}", requestId, userId);
        return new ResponseEntity<>(privateRequestService.cancelRequest(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<Object> getEventRequests(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("get all requests fro event with id = {} by event initiator with id = {}", eventId, userId);
        return new ResponseEntity<>(privateRequestService.getEventRequests(userId, eventId), HttpStatus.OK);
    }


}
