package ru.practicum.ewm.service.mapping;

import org.mapstruct.Mapper;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.domain.UserAction;
import ru.practicum.ewm.dto.rating.UserActionDto;

@Mapper(componentModel = "spring")
public interface UserActionMapping {
    UserActionDto toDto(UserAction userAction);

    default Long convertEventToLong(Event event) {
        if (event != null) {
            return event.getId();
        }
        return null;
    }

    default Long convertEventToLong(User user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
