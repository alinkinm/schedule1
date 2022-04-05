package net.schedule.service;

import javafx.util.Pair;
import net.schedule.model.Client;
import net.schedule.model.Event;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;


public interface ClientService {

    Client getById(Long id);

    void save(Client client);

    void delete(Long id);

    List<Client> getAll();

    List<Event> getSchedule(Long id, Date day);

    List<Pair<LocalTime, LocalTime>> getFreeTime(Long id, Date day);

    void saveEvent(Event event);

    Boolean addSharedEvent(Event event, List<Long> ids);

    List<Pair<LocalTime, LocalTime>> getSharedFreeTime(List<Long> ids, Date day);
}
