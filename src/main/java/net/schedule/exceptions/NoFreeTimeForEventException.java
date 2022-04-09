package net.schedule.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoFreeTimeForEventException extends RuntimeException {
    public NoFreeTimeForEventException() {
        super("There is no free time for your event. Check your schedule again.");
    }
}
