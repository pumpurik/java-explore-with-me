package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

//    @Query("SELECT e FROM Event e " +
//            "WHERE e.state = 'PUBLISHED' " +
//            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ) " +
//            "OR (:categories IS NULL OR e.category.id IN :categories) " +
//            "OR ((:paid IS NULL) OR (e.paid = :paid)) " +
//            "OR ((cast(:rangeStart as date) IS NULL OR cast(:rangeEnd as date) IS NULL) OR " +
//            "(e.eventDate BETWEEN cast(:rangeStart as timestamp) AND cast(:rangeEnd as timestamp)))")
//    List<Event> findAllByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
//                                LocalDateTime rangeEnd);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ) " +
            "OR (:categories IS NULL OR e.category.id IN :categories) " +
            "OR (:paid IS NULL OR e.paid = :paid) " +
            "OR ((cast(:rangeStart as date) IS NULL OR cast(:rangeEnd as date) IS NULL) OR " +
            "(e.eventDate BETWEEN cast(:rangeStart as timestamp) AND cast(:rangeEnd as timestamp)))")
    List<Event> findAllByPublic(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Sort sort);

    @Query("SELECT e FROM Event e WHERE " +
            "(LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')) ) " +
            "AND (COALESCE(:categories, NULL) IS NULL OR e.category.id IN :categories) " +
            "AND (COALESCE(:paid, NULL) IS NULL OR e.paid = :paid) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR COALESCE(:rangeEnd, NULL) IS NULL OR " +
            "(e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date)))")
    Page<Event> findAllByPublic(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (LOWER(e.annotation) LIKE LOWER(CONCAT('%', COALESCE(:text, ''), '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', COALESCE(:text, ''), '%')) ) " +
            "AND (COALESCE(:categories, :emptyList) = :emptyList OR e.category.id IN :categories) " +
            "AND (COALESCE(:paid, false) = false OR e.paid = :paid) " +
            "AND ((COALESCE(:rangeStart, '') = '' OR COALESCE(:rangeEnd, '') = '') OR (e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date)))")
    Page<Event> findAllByPublicOnlyAvailable(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("emptyList") List<Long> emptyList,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (COALESCE(:states, :emptyList) = :emptyList OR e.state IN :states) " +
            "AND (COALESCE(:users, :emptyList) = :emptyList OR e.initiator.id IN :users) " +
            "AND (COALESCE(:categories, :emptyList) = :emptyList OR e.category.id IN :categories) " +
            "AND ((COALESCE(:rangeStart, '') = '' OR COALESCE(:rangeEnd, '') = '') OR (e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date)))"
    )
    List<Event> findAllByAdmin(List<Long> users, List<EventStateEnum> states, List<Long> categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, @Param("emptyList") List<Long> emptyList, Pageable pageable);

}
