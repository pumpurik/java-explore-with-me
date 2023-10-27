package ru.practicum.ewm.urils;

import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.enums.StateActionEnum;

public class ConvertUtils {
    public static EventStateEnum convertToState(StateActionEnum stateActionEnum) {
        if (stateActionEnum == StateActionEnum.PUBLISH_EVENT) return EventStateEnum.PUBLISHED;
        if (stateActionEnum == StateActionEnum.REJECT_EVENT) return EventStateEnum.CANCELED;
        if (stateActionEnum == StateActionEnum.CANCEL_REVIEW) return EventStateEnum.CANCELED;
        return EventStateEnum.PENDING;
    }
}
