package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.rating.UserActionDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.UserActionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/likes")
public class AdminLikeEventController {
    private final UserActionService userActionService;

    @GetMapping("users/{userId}")
    ResponseEntity<UserActionDto> getAction(@PathVariable Long userId,
                                            @RequestParam Long eventId) throws NotFoundException {
        return new ResponseEntity<>(userActionService.getAction(userId, eventId), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<UserActionDto>> getActions(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(userActionService.getActions(PageRequest.of(from, size)), HttpStatus.OK);
    }
}
