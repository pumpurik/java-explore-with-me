package ru.practicum.ewm.dto.rating;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.ActionTypeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActionDto {
    Long user;
    Long event;
    ActionTypeEnum actionType;
}
