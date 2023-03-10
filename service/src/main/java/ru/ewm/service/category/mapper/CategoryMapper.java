package ru.ewm.service.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.category.dto.NewCategoryDto;
import ru.ewm.service.category.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
