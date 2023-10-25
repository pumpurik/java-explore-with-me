package ru.practicum.ewm.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.ParticipationRequestStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "participation_requests", schema = "public")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "created")
    LocalDateTime created;
    @JoinColumn(name = "event_id")
    @ManyToOne
    Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    ParticipationRequestStatusEnum status;
}
