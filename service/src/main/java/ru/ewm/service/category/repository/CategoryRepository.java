package ru.ewm.service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
