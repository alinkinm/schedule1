package net.schedule.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.schedule.model.Event;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Просто сущность для свободного времени")
public class FreeTimeInterval {

    private LocalTime start;
    private LocalTime finish;

    public static FreeTimeInterval from(LocalTime start, LocalTime finish) {
        return FreeTimeInterval.builder()
                .start(start)
                .finish(finish)
                .build();
    }

}
