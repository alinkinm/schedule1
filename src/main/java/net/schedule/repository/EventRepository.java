package net.schedule.repository;

import net.schedule.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value="select * from event where client_id=?1 and day=?2", nativeQuery = true)
    List<Event> getSchedule(Long id, Date day);
}
