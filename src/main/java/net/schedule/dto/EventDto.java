package net.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.schedule.model.Event;

import java.sql.Date;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Event")
public class EventDto {

    @Schema(description = "Идентификатор", example = "1")
    private Long id;

    @Schema(description="Название", example = "День рождения")
    private String name;

    @Schema(description="Идентификатор человека", example = "1")
    private Long client_id;

    @Schema(description="Начало события", example = "10:00")
    private LocalTime start_time;

    @Schema(description="Окончание события", example = "11:00")
    private LocalTime finish_time;

    @Schema(description="День события", example = "2022-02-02")
    private Date day;

    public static EventDto from(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .client_id(event.getClient_id())
                .start_time(event.getStart_time())
                .finish_time(event.getFinish_time())
                .day(event.getDay())
                .build();
    }
}
