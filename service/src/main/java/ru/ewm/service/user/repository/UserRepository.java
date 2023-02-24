package ru.ewm.service.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIsGreaterThanEqual(Long id, Pageable pageable);
}
