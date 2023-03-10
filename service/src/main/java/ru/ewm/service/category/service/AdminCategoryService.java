package ru.ewm.service.category.service;

import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.category.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);
}
