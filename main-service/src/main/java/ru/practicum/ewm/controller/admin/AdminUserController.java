package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) Long[] ids,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return new ResponseEntity<>(userService.getUsers(ids == null ? Collections.emptyList() : Arrays.asList(ids), from, size),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return new ResponseEntity<>(userService.createUser(newUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws NotFoundException {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
