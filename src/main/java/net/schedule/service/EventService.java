package net.schedule.service;

import javafx.util.Pair;
import net.schedule.dto.EventDto;
import net.schedule.dto.FreeTimeInterval;
import net.schedule.dto.Schedule;
import net.schedule.model.Event;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

public interface EventService {
    Schedule getSchedule(Long id, Date day);

    List<FreeTimeInterval> getFreeTime(Long id, Date day);

    EventDto saveEvent(EventDto event);

    EventDto addSharedEvent(EventDto event, List<Long> ids);

    List<FreeTimeInterval> getSharedFreeTime(List<Long> ids, Date day);

    Boolean checkFreeTimeForNewEvent(Event event);
}
