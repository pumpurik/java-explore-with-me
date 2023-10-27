package ru.practicum.ewm.service.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapping {
    UserDto userToDto(User user);

    UserShortDto userToUserShortDto(User user);

    NewUserRequest userToNewUserRequest(User user);

    @InheritInverseConfiguration
    User dtoToUser(UserDto userDto);

    @InheritInverseConfiguration
    User userShortDtoToUser(UserShortDto userShortDto);

    @InheritInverseConfiguration
    User newUserRequestToUser(NewUserRequest newUserRequest);

}
