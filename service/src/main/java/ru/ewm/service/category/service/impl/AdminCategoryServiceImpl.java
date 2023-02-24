package ru.ewm.service.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.category.dto.NewCategoryDto;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.category.repository.CategoryRepository;
import ru.ewm.service.category.service.AdminCategoryService;
import ru.ewm.service.error.EntityNotFoundException;

import java.util.Optional;

import static ru.ewm.service.category.mapper.CategoryMapper.toCategory;
import static ru.ewm.service.category.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category categoryToSave = toCategory(newCategoryDto);
        return toCategoryDto(categoryRepository.save(categoryToSave));
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        try {
            categoryRepository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(catId, Category.class.getSimpleName());
        }
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        Optional<Category> foundCategory = categoryRepository.findById(catId);
        if (foundCategory.isEmpty()) {
            throw new EntityNotFoundException(catId, Category.class.getSimpleName());

        }
        Category categoryToUpdate = foundCategory.get();
        categoryToUpdate.setName(newCategoryDto.getName());
        return toCategoryDto(categoryRepository.save(categoryToUpdate));
    }
}
