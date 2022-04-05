package net.schedule;

import javafx.util.Pair;
import net.schedule.service.ClientServiceImpl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ClientServiceImpl clientService = new ClientServiceImpl();
        List<Pair<LocalTime, LocalTime>> alina = new ArrayList<>();
        LocalTime start1 = LocalTime.of(10,0);
        LocalTime fn1 = LocalTime.of(16,0);
        Pair<LocalTime, LocalTime> pair = new Pair<>(start1, fn1);
        alina.add(pair);
        List<Pair<LocalTime, LocalTime>> alina1 = new ArrayList<>();
        LocalTime st = LocalTime.of(9,0);
        LocalTime fn = LocalTime.of(14,0);
        Pair<LocalTime, LocalTime> pair1 = new Pair<>(st, fn);
        alina1.add(pair1);
        System.out.println(clientService.checkSharedFreeTime(alina, alina1));
    }
}
