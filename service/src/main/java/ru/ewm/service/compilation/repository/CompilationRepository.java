package ru.ewm.service.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
