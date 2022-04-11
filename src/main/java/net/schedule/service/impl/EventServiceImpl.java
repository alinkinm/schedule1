package net.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import net.schedule.dto.EventDto;
import net.schedule.dto.FreeTimeInterval;
import net.schedule.dto.Schedule;
import net.schedule.exceptions.ClientNotFoundException;
import net.schedule.exceptions.NoFreeTimeForEventException;
import net.schedule.model.Event;
import net.schedule.repository.ClientRepository;
import net.schedule.repository.EventRepository;
import net.schedule.service.EventService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final ClientRepository clientRepository;

    @Override
    public EventDto saveEvent(EventDto event) {
        Event newEvent = Event.builder()
                .name(event.getName())
                .client_id(event.getClient_id())
                .start_time(event.getStart_time())
                .finish_time(event.getFinish_time())
                .day(event.getDay())
                .build();
        clientRepository
                .findById(event.getClient_id())
                .orElseThrow((Supplier<RuntimeException>) ClientNotFoundException::new);
        if (checkFreeTimeForNewEvent(event)) {
            return EventDto.from(eventRepository.save(newEvent));
        }
        else {
            throw new NoFreeTimeForEventException();
        }

    }

    @Override
    public Schedule getSchedule(Long id, Date day) {
        clientRepository
                .findById(id)
                .orElseThrow((Supplier<RuntimeException>) ClientNotFoundException::new);
        Schedule schedule = new Schedule();
        schedule.setDay(day);
        List<EventDto> eventsInSchedule = new ArrayList<>();
        List<Event> list = eventRepository.getSchedule(id, day);
        for (Event e:list) {
            eventsInSchedule.add(EventDto.from(e));
        }
        schedule.setSchedule(eventsInSchedule);
        return schedule;
    }

    @Override
    public List<FreeTimeInterval> getFreeTime(Long id, Date day) {
        clientRepository
                .findById(id)
                .orElseThrow((Supplier<RuntimeException>) ClientNotFoundException::new);
        List<Event> schedule = eventRepository.getSchedule(id,day);
        FreeTimeInterval dayStarting = FreeTimeInterval.from(LocalTime.of(0,0),
                LocalTime.of(0,0));

        List<FreeTimeInterval> intervals = new ArrayList<>();
        intervals.add(dayStarting);

        for (Event e: schedule) {
            FreeTimeInterval interval = FreeTimeInterval.from(e.getStart_time(), e.getFinish_time());
            intervals.add(interval);
        }
        FreeTimeInterval dayFinishing = FreeTimeInterval.from(LocalTime.of(23,59),
                LocalTime.of(23,59));
        intervals.add(dayFinishing);

        List<FreeTimeInterval> freeTimeList = new ArrayList<>();
        for (int i = 0; i< intervals.size()-1; i++) {
            LocalTime endEvent = intervals.get(i).getFinish();
            Duration freeTimeDuration = Duration.between(endEvent, intervals.get(i+1).getStart());
            LocalTime startNewEvent = (endEvent.plus(freeTimeDuration));
            FreeTimeInterval freeTime = FreeTimeInterval.from(endEvent, startNewEvent);
            freeTimeList.add(freeTime);
        }
        return freeTimeList;
    }

    @Override
    public EventDto addSharedEvent(EventDto event, List<Long> ids) {
        for (Long id: ids) {
            event.setId(id);
            if (!checkFreeTimeForNewEvent(event)) {
                throw new NoFreeTimeForEventException();
            }
            saveEvent(event);
        }
        return event;
    }

    @Override
    public List<FreeTimeInterval> getSharedFreeTime(List<Long> ids, Date day) {
        List<FreeTimeInterval> firstList = getFreeTime(ids.get(0), day);
        for (long id=1; id < ids.size();id++) {
            List<FreeTimeInterval> secondList = getFreeTime(ids.get((int)id), day);
            firstList = checkSharedFreeTime(firstList, secondList);
        }
        return firstList;
    }

    public List<FreeTimeInterval> checkSharedFreeTime(List<FreeTimeInterval> firstList,
                                                                List<FreeTimeInterval> secondList){
        List<FreeTimeInterval> resultList = new ArrayList<>();
        for (FreeTimeInterval firstInterval: firstList) {
            for (FreeTimeInterval secondInterval: secondList) {
                if (firstInterval.getFinish().compareTo(secondInterval.getStart())>0 &&
                        firstInterval.getStart().compareTo(secondInterval.getFinish())<0){
                    if (firstInterval.getStart().compareTo(secondInterval.getStart())<=0 &&
                            firstInterval.getFinish().compareTo(secondInterval.getFinish())>=0) {
                        resultList.add(secondInterval);

                    }
                    else if (secondInterval.getStart().compareTo(firstInterval.getStart())<0 &&
                            secondInterval.getFinish().compareTo(firstInterval.getFinish())>=0) {
                        resultList.add(firstInterval);
                    }

                    else if (firstInterval.getStart().compareTo(secondInterval.getStart())<=0 &&
                            firstInterval.getFinish().compareTo(secondInterval.getFinish())<=0) {
                        FreeTimeInterval res = FreeTimeInterval.from(secondInterval.getStart(),firstInterval.getFinish());
                        resultList.add(res);
                    }

                    else if (firstInterval.getStart().compareTo(secondInterval.getStart())>=0 &&
                            firstInterval.getFinish().compareTo(secondInterval.getFinish())>=0) {
                        FreeTimeInterval res = FreeTimeInterval.from(firstInterval.getStart(),secondInterval.getFinish());
                        resultList.add(res);
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public Boolean checkFreeTimeForNewEvent(EventDto event) {
        FreeTimeInterval eventTime = FreeTimeInterval.from(event.getStart_time(), event.getFinish_time());
        List<FreeTimeInterval> freeTimeList = getFreeTime(event.getClient_id(), event.getDay());
        for (FreeTimeInterval interval: freeTimeList) {
            if (eventTime.getStart().compareTo(interval.getStart()) >= 0 &&
                    eventTime.getFinish().compareTo(interval.getFinish()) <= 0) {
                return true;
            }
        }
        return false;
    }
}
