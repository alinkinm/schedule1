package net.schedule.service;

import net.schedule.dto.EventDto;
import net.schedule.dto.FreeTimeInterval;
import net.schedule.dto.Schedule;

import java.sql.Date;
import java.util.List;

public interface EventService {
    Schedule getSchedule(Long id, Date day);

    List<FreeTimeInterval> getFreeTime(Long id, Date day);

    EventDto saveEvent(EventDto event);

    EventDto addSharedEvent(EventDto event, List<Long> ids);

    List<FreeTimeInterval> getSharedFreeTime(List<Long> ids, Date day);

    Boolean checkFreeTimeForNewEvent(EventDto event);
}
