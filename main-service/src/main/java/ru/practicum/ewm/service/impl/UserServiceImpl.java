package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.service.mapping.UserMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        boolean b = from != null && size != null;
        if (ids.isEmpty()) {
            return b ? userRepository.findAll(PageRequest.of(from, size)).map(userMapping::userToDto).getContent()
                    : userRepository.findAll().stream().map(userMapping::userToDto).collect(Collectors.toList());
        }
        return b ? userRepository.findByIdIn(ids, PageRequest.of(from, size)).stream().map(userMapping::userToDto)
                .collect(Collectors.toList()) : userRepository.findByIdIn(ids).stream().map(userMapping::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return userMapping.userToDto(userRepository.save(userMapping.newUserRequestToUser(newUserRequest)));
    }

    @Override
    public void deleteUser(Long userId) throws NotFoundException {
        userRepository.delete(userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        }));
    }
}
