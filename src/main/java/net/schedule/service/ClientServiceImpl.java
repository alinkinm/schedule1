package net.schedule.service;

import javafx.util.Pair;
import net.schedule.model.Client;
import net.schedule.model.Event;
import net.schedule.repository.ClientRepository;
import net.schedule.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EventRepository eventRepository;


    @Override
    public Client getById(Long id) {
        return clientRepository.getById(id);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Event> getSchedule(Long id, Date day) {
        System.out.println("уровень клайентсервисимпл"+eventRepository.getSchedule(id, day));
        return eventRepository.getSchedule(id, day);
    }

    @Override
    public List<Pair<LocalTime, LocalTime>> getFreeTime(Long id, Date day) {
        List<Event> schedule = eventRepository.getSchedule(id,day);
        LocalTime startOfTheDay = LocalTime.of(0,0);
        LocalTime EndOfTheDay = LocalTime.of(23,59);
        Pair<LocalTime, LocalTime> startingDay = new Pair<>(startOfTheDay,
                startOfTheDay);

        List<Pair<LocalTime, LocalTime>> intervals = new ArrayList<>();
        intervals.add(startingDay);

        //формирование списка интервалов
        for (Event e: schedule) {
            LocalTime start = e.getStart_time();
            LocalTime finish = e.getFinish_time();
            Pair<LocalTime,LocalTime> interval = new Pair<>(start, finish);
            intervals.add(interval);
        }
        Pair<LocalTime, LocalTime> finishingDay = new Pair<>(EndOfTheDay,
                EndOfTheDay);
        intervals.add(finishingDay);

        //создаем список из пар - время начала свободного времени + длительность. время начала -
        //ПЕРВЫЙ элемент LocalTime- intervals.get(i).getValue(), то есть время окончания события,
        // второй элемент - длительность -
        // Duration duration = Duration.between(intervals.get(i).getValue(), intervals.get(i+1).getKey);

        List<Pair<LocalTime, LocalTime>> freeTimeList = new ArrayList<>();
        for (int i = 0; i< intervals.size()-1; i++) {
            LocalTime endEvent = intervals.get(i).getValue();
            Duration freeTimeDuration = Duration.between(endEvent, intervals.get(i+1).getKey());
            LocalTime startnNewEvent = (endEvent.plus(freeTimeDuration));
            Pair<LocalTime, LocalTime> freeTime = new Pair<>(endEvent, startnNewEvent);
            freeTimeList.add(freeTime);
         }

        List<String> result = new ArrayList<>();
        for (Pair<LocalTime, LocalTime> pair: freeTimeList) {
            result.add( pair.getKey().toString()+" - "+ pair.getValue().toString());
        }
        return freeTimeList;
    }

    //добавление одного события с обработкой временных колллизий
    @Override
    public void saveEvent(Event event) {
        Pair<LocalTime, LocalTime> eventTime = new Pair<>(event.getStart_time(), event.getFinish_time());
        List<Pair<LocalTime, LocalTime>> freeTimeList = getFreeTime(event.getClient_id(), event.getDay());
        for (Pair<LocalTime, LocalTime> interval: freeTimeList) {
            if (eventTime.getKey().compareTo(interval.getKey()) >= 0 &&
                    eventTime.getValue().compareTo(interval.getValue()) <= 0) {
                eventRepository.save(event);
            }
        }
    }

    @Override
    public Boolean addSharedEvent(Event event, List<Long> ids) {
        return false;

    }

    public List<Pair<LocalTime, LocalTime>> checkSharedFreeTime(List<Pair<LocalTime, LocalTime>> firstList,
                                                                List<Pair<LocalTime, LocalTime>> secondList){
        List<Pair<LocalTime, LocalTime>> resultList = new ArrayList<>();
        for (Pair<LocalTime, LocalTime> firstInterval: firstList) {
            for (Pair<LocalTime, LocalTime> secondInterval: secondList) {
                if (firstInterval.getValue().compareTo(secondInterval.getKey())>0 &&
                firstInterval.getKey().compareTo(secondInterval.getValue())<0){
                    if (firstInterval.getKey().compareTo(secondInterval.getKey())<=0 &&
                            firstInterval.getValue().compareTo(secondInterval.getValue())>=0) {
                        resultList.add(secondInterval);

                    }
                    else if (secondInterval.getKey().compareTo(firstInterval.getKey())<0 &&
                            secondInterval.getValue().compareTo(firstInterval.getValue())>=0) {
                        resultList.add(firstInterval);
                    }
                    //первый смещается влево
                    else if (firstInterval.getKey().compareTo(secondInterval.getKey())<=0 &&
                            firstInterval.getValue().compareTo(secondInterval.getValue())<=0) {
                        Pair<LocalTime, LocalTime> res = new Pair<>(secondInterval.getKey(),firstInterval.getValue());
                        resultList.add(res);
                    }
                    //второй смещается влево
                    else if (firstInterval.getKey().compareTo(secondInterval.getKey())>=0 &&
                            firstInterval.getValue().compareTo(secondInterval.getValue())>=0) {
                        Pair<LocalTime, LocalTime> res = new Pair<>(firstInterval.getKey(),secondInterval.getValue());
                        resultList.add(res);
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<Pair<LocalTime, LocalTime>> getSharedFreeTime(List<Long> ids, Date day) {
        List<Pair<LocalTime, LocalTime>> firstList = getFreeTime(ids.get(0), day);
        System.out.println(firstList);
        for (long id=1; id < ids.size();id++) {
            List<Pair<LocalTime, LocalTime>> secondList = getFreeTime(ids.get((int)id), day);
            System.out.println(secondList);
            firstList = checkSharedFreeTime(firstList, secondList);
            System.out.println(firstList);
        }
        return firstList;
    }
}
