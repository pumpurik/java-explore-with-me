package ru.practicum.ewm.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStateEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events", schema = "public")
@ToString(exclude = {"confirmedRequests", "category", "initiator"}, callSuper = false)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "annotation", columnDefinition = "TEXT")
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "event")
    List<ParticipationRequest> confirmedRequests;
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
    Integer participantLimit;
    @Column(name = "publishedOn")
    LocalDateTime publishedOn;
    @Column(name = "requestModeration")
    boolean requestModeration;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventStateEnum state = EventStateEnum.PENDING;
    @Column(name = "title")
    String title;

//    @ManyToMany(mappedBy = "events")
//    Set<Compilation> compilation = new LinkedHashSet<>();
}
