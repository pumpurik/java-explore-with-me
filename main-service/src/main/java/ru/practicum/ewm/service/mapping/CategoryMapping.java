package ru.practicum.ewm.service.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapping {
    CategoryDto categoryToCategoryDto(Category category);

    NewCategoryDto categoryToNewCategoryDto(Category category);

    @InheritInverseConfiguration
    Category categoryDtoToCategory(CategoryDto categoryDto);

    @InheritInverseConfiguration
    Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto);
}
