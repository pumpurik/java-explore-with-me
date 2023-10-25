package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    List<User> findByIdIn(List<Long> ids);
}
