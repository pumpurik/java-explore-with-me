package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final StatsClient statsClient;
    private final String path = "/{compId}";

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody(required = false) @Valid NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path)
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) throws NotFoundException {
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path)
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId, @Valid @RequestBody(required = false) UpdateCompilationRequest updateCompilationRequest) throws NotFoundException {
        return new ResponseEntity<>(compilationService.updateCompilation(compId, updateCompilationRequest), HttpStatus.OK);
    }
}
