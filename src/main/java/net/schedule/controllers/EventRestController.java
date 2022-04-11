package net.schedule.controllers;

import lombok.RequiredArgsConstructor;
import net.schedule.dto.EventDto;
import net.schedule.dto.FreeTimeInterval;
import net.schedule.dto.Schedule;
import net.schedule.service.impl.EventServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/schedule")
public class EventRestController {

    private final EventServiceImpl eventService;

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> saveEvent(@RequestBody EventDto event) {
        System.out.println(event);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.saveEvent(event));

    }

    @GetMapping("/{id}/{day}")
    public ResponseEntity<Schedule> showSchedule(@PathVariable("id") Long clientId, @PathVariable("day") Date day) {
        return ResponseEntity.ok(eventService.getSchedule(clientId,day));
    }

    @GetMapping("/free/{id}/{day}")
    public ResponseEntity<List<FreeTimeInterval>> showFreeTime(@PathVariable("id") Long clientId, @PathVariable("day") Date day) {
        return ResponseEntity.ok(eventService.getFreeTime(clientId,day));
    }

    @PostMapping("/free/{day}")
    public ResponseEntity<List<FreeTimeInterval>> showAllFreeTime(@RequestBody List<Long> list,
                                                                            @PathVariable  Date day) {
        return ResponseEntity.ok(eventService.getSharedFreeTime(list,day));
    }

    @PostMapping("/{list}")
    public ResponseEntity<EventDto> saveSharedEvent(@RequestBody EventDto event, @PathVariable List<Long> list) {
        return ResponseEntity.ok(eventService.addSharedEvent(event,list));
    }


}
