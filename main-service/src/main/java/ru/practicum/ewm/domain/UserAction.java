package ru.practicum.ewm.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.ActionTypeEnum;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "rating_events", schema = "public")
//@ToString(exclude = {"ratingEvent"}, callSuper = false)
public class UserAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @JoinColumn(name = "event_id")
    @ManyToOne
    Event event;
    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    ActionTypeEnum actionType;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "rating_event_id")
//    RatingEvent ratingEvent;

}
