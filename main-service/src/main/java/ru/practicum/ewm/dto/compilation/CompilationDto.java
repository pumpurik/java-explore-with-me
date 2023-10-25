package ru.practicum.ewm.dto.compilation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.event.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    List<EventShortDto> events;
    @NotNull
    int id;
    @NotNull
    boolean pinned;
    @NotNull
    String title;

}
