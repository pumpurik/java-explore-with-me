package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.enums.EventStateEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByInitiatorAndId(User initiator, Long id);

    List<Event> findByInitiator(User user, Pageable pageable);

    List<Event> findByInitiator(User user);

    List<Event> findByIdIn(List<Long> ids);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((cast(:rangeStart as date) IS NULL OR cast(:rangeEnd as date) IS NULL) OR " +
            "(e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date)))")
    List<Event> findAllByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd);

    //    @Query("SELECT e FROM Event e " +
//            "WHERE e.state = 'PUBLISHED' " +
//            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ) " +
//            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
//            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
//            "AND ((cast(:rangeStart as date) IS NULL OR cast(:rangeEnd as date) IS NULL) OR " +
//            "(e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date))) LIMIT :size OFFSET :from")
//    List<Event> findAllByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
//                                LocalDateTime rangeEnd, Integer from, Integer size);
    Page<Event> findByStateAndCategoryIdInAndPaidAndEventDateBetweenAndAnnotationContainingOrDescriptionContaining(
            EventStateEnum state, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, String text, String textDescription, Pageable pageable
    );

    @Query("SELECT e FROM Event e " +
            "WHERE (COALESCE(:states, e.state) = e.state) " +
            "AND (COALESCE(:users, e.initiator.id) = e.initiator.id) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:rangeStart IS NULL OR :rangeEnd IS NULL) OR " +
            "(e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date)))"
    )
    List<Event> findAllByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd);

    Page<Event> findAllByStateInAndInitiatorIdInAndCategoryIdInAndEventDateBetween(
            List<EventStateEnum> states, List<Long> users, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable
    );
}
