package ru.ewm.service.category.service;

import ru.ewm.service.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getCategories(Long from, Integer size);
    CategoryDto getCategoryById(Long catId);
}
