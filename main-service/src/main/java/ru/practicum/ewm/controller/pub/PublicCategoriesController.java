package ru.practicum.ewm.controller.pub;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoriesController {
    private final CategoryService categoryService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return new ResponseEntity<>(categoryService.getCategories(PageRequest.of(from, size)), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) throws NotFoundException {
        return new ResponseEntity<>(categoryService.getCategory(catId), HttpStatus.OK);
    }

}
