package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {
    private final CompilationService compilationService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(compilationService.getCompilations(PageRequest.of(from, size)), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) throws NotFoundException {
        return new ResponseEntity<>(compilationService.getCompilation(compId), HttpStatus.OK);
    }

}
