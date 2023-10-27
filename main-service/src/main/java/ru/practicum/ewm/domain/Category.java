package ru.practicum.ewm.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "categories", schema = "public")
@ToString(exclude = {"events"}, callSuper = false)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "name", length = 50, unique = true)
    String name;
    @OneToMany(mappedBy = "category")
    List<Event> events;
}
