package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategory(Long catId) throws NotFoundException;

    CategoryDto createCategory(NewCategoryDto newCategoryDto) throws ConflictException;

    void deleteCategory(Long catId) throws NotFoundException, ConflictException;

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) throws NotFoundException, ConflictException;
}
