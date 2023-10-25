package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.mapping.CategoryMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapping categoryMapping;


    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(categoryMapping::categoryToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) throws NotFoundException {
        return categoryMapping.categoryToCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("", catId);
            return new NotFoundException(String.format("", catId));
        }));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoryMapping.categoryToCategoryDto(categoryRepository.save(categoryMapping.newCategoryDtoToCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) throws NotFoundException {
        categoryRepository.delete(categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("", catId);
            return new NotFoundException(String.format("", catId));
        }));
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) throws NotFoundException {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            log.info("", catId);
            return new NotFoundException(String.format("", catId));
        });
        category.setName(newCategoryDto.getName());
        return categoryMapping.categoryToCategoryDto(categoryRepository.save(category));
    }
}
