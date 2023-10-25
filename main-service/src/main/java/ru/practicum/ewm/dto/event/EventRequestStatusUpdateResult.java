package ru.practicum.ewm.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.ParticipationRequestDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResult {
    ParticipationRequestDto confirmedRequests;
    ParticipationRequestDto rejectedRequests;
}
