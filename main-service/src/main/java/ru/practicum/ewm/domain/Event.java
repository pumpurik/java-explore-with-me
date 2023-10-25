package ru.practicum.ewm.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStateEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "annotation", columnDefinition = "TEXT")
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Column(name = "confirmedRequests")
    int confirmedRequests;
    @Column(name = "createdOn")
    LocalDateTime createdOn;
    @Column(name = "description", columnDefinition = "TEXT")
    String description;
    @Column(name = "eventDate")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    Location location;
    @Column(name = "paid")
    boolean paid;
    @Column(name = "participantLimit")
    int participantLimit;
    @Column(name = "publishedOn")
    LocalDateTime publishedOn;
    @Column(name = "requestModeration")
    boolean requestModeration;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventStateEnum state = EventStateEnum.PENDING;
    @Column(name = "title")
    String title;
    @Column(name = "views")
    int views;

    @ManyToOne
    @JoinColumn(name = "compilation_id")
    Compilation compilation;
}
