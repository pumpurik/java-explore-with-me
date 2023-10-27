package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.ParticipationRequest;
import ru.practicum.ewm.domain.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
//    List<ParticipationRequest> findAllByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByEventAndRequester(Event event, User user);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long userId);

    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByIdIn(List<Long> ids);
}
