package net.schedule.rest;


import javafx.util.Pair;
import net.schedule.model.Client;
import net.schedule.model.Event;
import net.schedule.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ClientRestController {

    @Autowired
    private ClientServiceImpl clientService;

    public ClientRestController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value="client", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> saveClient(@RequestBody @Validated Client client) {
        HttpHeaders headers = new HttpHeaders();

        if (client==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.clientService.save(client);

        return new ResponseEntity<>(client, headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "schedule/{id}/{day}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> showSchedule(@PathVariable("id") Long clientId, @PathVariable("day") Date day) {
        System.out.println(clientId.getClass());
        System.out.println(day.getClass());
        List<Event> events = this.clientService.getSchedule(clientId, day);
        System.out.println(events);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @RequestMapping(value = "free/{id}/{day}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pair<LocalTime, LocalTime>>> showFreeTime(@PathVariable("id") Long clientId, @PathVariable("day") Date day) {
        List<Pair<LocalTime, LocalTime>> freeTime = this.clientService.getFreeTime(clientId, day);

        if (freeTime.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(freeTime, HttpStatus.OK);
    }

    @RequestMapping(value = "free/{day}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pair<LocalTime, LocalTime>>> showAllFreeTime(@RequestBody List<Long> list,
                                                                         @PathVariable  Date day) {
        List<Pair<LocalTime, LocalTime>> freeTime = this.clientService.getSharedFreeTime(list, day);
        if (freeTime.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(freeTime, HttpStatus.OK);
    }

    @RequestMapping(value="event", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> saveEvent(@RequestBody @Validated Event event) {
        HttpHeaders headers = new HttpHeaders();

        if (event==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.clientService.saveEvent(event);

        return new ResponseEntity<>(event, headers, HttpStatus.CREATED);
    }



}
