package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.domain.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);
}
