package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.LocationDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Long category;
    @Size(min = 20, max = 7000)
    @NotNull
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    @FutureOrPresent
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    @NotBlank
    String title;
}
