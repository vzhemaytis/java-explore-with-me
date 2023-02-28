package ru.ewm.service.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.category.mapper.CategoryMapper;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.category.repository.CategoryRepository;
import ru.ewm.service.category.service.PublicCategoryService;
import ru.ewm.service.error.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ewm.service.category.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<CategoryDto> getCategories(Long from, Integer size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Category> foundCategories = categoryRepository
                .findAllByIdIsGreaterThanEqualOrderByIdAsc(from, pageRequest);
        return foundCategories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryDto getCategoryById(Long catId) {
        Optional<Category> foundCategory = categoryRepository.findById(catId);
        if (foundCategory.isEmpty()) {
            throw new EntityNotFoundException(catId, Category.class.getSimpleName());
        }
        return toCategoryDto(categoryRepository.save(foundCategory.get()));
    }
}
