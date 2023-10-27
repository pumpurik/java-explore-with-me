package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.domain.Compilation;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.mapping.CompilationMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.urils.NullUtils.getOrDefault;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapping compilationMapping;


    @Override
    public List<CompilationDto> getCompilations(PageRequest pageRequest) {
        return compilationRepository.findAll(pageRequest).stream()
                .map(compilationMapping::compilationToDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) throws NotFoundException {
        return compilationMapping.compilationToDto(compilationRepository.findById(compId).orElseThrow(() -> {
            log.info("Подборка событий c айди {} не найдена!", compId);
            return new NotFoundException(String.format("Подборка событий c айди %s не найдена!", compId));
        }));
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> byIdIn = Collections.emptyList();
        if (newCompilationDto != null) {
            byIdIn = eventRepository.findByIdIn(newCompilationDto.getEvents() == null ? Collections.emptyList() : newCompilationDto.getEvents());
        }

        return compilationMapping.compilationToDto(compilationRepository.save(compilationMapping.updateCompilationRequestToCompilation(newCompilationDto,
                byIdIn.isEmpty() ? Collections.emptyList() : byIdIn)));
    }

    @Override
    public void deleteCompilation(Long compId) throws NotFoundException {
        compilationRepository.delete(compilationRepository.findById(compId).orElseThrow(() -> {
            log.info("Подборка событий c айди {} не найдена!", compId);
            return new NotFoundException(String.format("Подборка событий c айди %s не найдена!", compId));
        }));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) throws NotFoundException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> {
            log.info("Подборка событий c айди {} не найдена!", compId);
            return new NotFoundException(String.format("Подборка событий c айди %s не найдена!", compId));
        });
        if (updateCompilationRequest != null) {
            compilation.setPinned(getOrDefault(updateCompilationRequest::isPinned, compilation.isPinned()));
            compilation.setTitle(getOrDefault(updateCompilationRequest::getTitle, compilation.getTitle()));
            compilation.getEvents().addAll(eventRepository.findByIdIn(updateCompilationRequest.getEvents() == null ? Collections.emptyList() : updateCompilationRequest.getEvents()));
        }
        return compilationMapping.compilationToDto(compilationRepository.save(compilation));
    }
}
