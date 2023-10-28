package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.rating.UserActionDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.UserActionService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class LikeEventController {
    private final UserActionService userActionService;

    @PostMapping("/{userId}/events/{eventId}/like")
    public ResponseEntity<UserActionDto> postLike(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) throws ConflictException, NotFoundException {
        return new ResponseEntity<>(userActionService.postLike(userId, eventId), HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/events/{eventId}/dislike")
    public ResponseEntity<UserActionDto> postDislike(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) throws ConflictException, NotFoundException {
        return new ResponseEntity<>(userActionService.postDislike(userId, eventId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/events/{eventId}/delete")
    public ResponseEntity<Void> deleteLikeOrDislike(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) throws ConflictException, NotFoundException {
        userActionService.deleteLikeOrDislike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
