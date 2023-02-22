package ru.ewm.service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
