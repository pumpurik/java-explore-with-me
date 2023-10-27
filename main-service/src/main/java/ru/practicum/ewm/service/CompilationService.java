package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(PageRequest of);

    CompilationDto getCompilation(Long compId) throws NotFoundException;

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId) throws NotFoundException;

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) throws NotFoundException;
}
