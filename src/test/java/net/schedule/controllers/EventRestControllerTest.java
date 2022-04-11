package net.schedule.controllers;

import net.schedule.dto.EventDto;
import net.schedule.dto.FreeTimeInterval;
import net.schedule.dto.Schedule;
import net.schedule.service.impl.EventServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("DiscountsController is working when")
public class EventRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventServiceImpl eventService;

    @BeforeEach
    public void setUp() {
        EventDto event1 = EventDto.builder()
                .client_id(1L)
                .name("Birthday party")
                .start_time(LocalTime.of(18,0))
                .finish_time(LocalTime.of(23,0))
                .day(Date.valueOf("2022-04-05"))
                .build();
        List<EventDto> events = new ArrayList<>();
        events.add(event1);

        Schedule schedule = Schedule.builder()
                .day(Date.valueOf("2022-04-05"))
                .schedule(events)
                .build();

        when(eventService.getSchedule(1L, Date.valueOf("2022-04-05"))).thenReturn(
                schedule);

        EventDto event = EventDto.builder()
                .client_id(1L)
                .name("Breakfast")
                .start_time(LocalTime.of(10,0))
                .finish_time(LocalTime.of(18,0))
                .day(Date.valueOf("2022-04-02"))
                .build();
        when(eventService.saveEvent(event)).thenReturn(event);

        List<FreeTimeInterval> freeTime = new ArrayList<>();
        FreeTimeInterval interval1 = FreeTimeInterval.from(LocalTime.of(0,0),
                LocalTime.of(10,0));
        FreeTimeInterval interval2 = FreeTimeInterval.from(LocalTime.of(18,0),
                LocalTime.of(23,59));
        freeTime.add(interval1);
        freeTime.add(interval2);

        when(eventService.getFreeTime(1L,Date.valueOf("2022-04-02"))).thenReturn(freeTime);

        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<FreeTimeInterval> result = Arrays.asList(FreeTimeInterval.from(LocalTime.of(13,0),
                        LocalTime.of(18,0)), FreeTimeInterval.from(LocalTime.of(21,0),
                                LocalTime.of(22,0)));
        when(eventService.getSharedFreeTime(ids, Date.valueOf("2022-04-02"))).thenReturn(result);


        when(eventService.addSharedEvent(event, ids)).thenReturn(event);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("showSchedule() is working")
    class ShowScheduleTest {

        @Test
        public void test_show_schedule() throws Exception {
            mockMvc.perform(get("/api/schedule/{id}/{day}", 1L, Date.valueOf("2022-04-05")))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.schedule[0].name", is("Birthday party")))
                    .andExpect(jsonPath("$.schedule[0].client_id", is(1)))
                    .andExpect(jsonPath("$.schedule[0].start_time", is("18:00:00")))
                    .andExpect(jsonPath("$.schedule[0].finish_time", is("23:00:00")))
                    .andExpect(jsonPath("$.schedule[0].day", is("2022-04-05")))
                    .andExpect(jsonPath("$.day", is("2022-04-05")));

        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("saveEvent() is working")
    class SaveEventTest {

        @Test
        public void test_save_event() throws Exception {
            mockMvc.perform(post("/api/schedule")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"name\" : \"Breakfast\",\"client_id\" : \"1\",\"start_time\" : \"10:00\",\"finish_time\" : \"18:00\",\"day\" : \"2022-04-02\"}"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is("Breakfast")))
                    .andExpect(jsonPath("$.client_id", is("1")))
                    .andExpect(jsonPath("$.start_time", is("10:00:00")))
                    .andExpect(jsonPath("$.finish_time", is("18:00:00")))
                    .andExpect(jsonPath("$.day", is("2022-04-02")));

        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("showFreeTime() is working")
    class ShowFreeTimeTest {

        @Test
        public void test_show_free_time() throws Exception {
            mockMvc.perform(get("/api/schedule/free/{id}/{day}", 1L, Date.valueOf("2022-04-02")))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].key", is("00:00:00")))
                    .andExpect(jsonPath("$[0].value", is("10:00:00")))
                    .andExpect(jsonPath("$[1].key", is("18:00:00")))
                    .andExpect(jsonPath("$[1].value", is("23:59:00")));
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("showAllFreeTime() is working")
    class ShowAllFreeTimeTest {

        @Test
        public void test_show_all_free_time() throws Exception {
            mockMvc.perform(post("/api/schedule/free/{day}", Date.valueOf("2022-04-02"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("[1,2,3]"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].start", is("13:00:00")))
                    .andExpect(jsonPath("$[0].finish", is("18:00:00")))
                    .andExpect(jsonPath("$[1].start", is("21:00:00")))
                    .andExpect(jsonPath("$[1].finish", is("22:00:00")))
                    ;
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("saveSharedEvent() is working")
    class SaveSharedEventTest {

        @Test
        public void test_save_shared_event() throws Exception {
            mockMvc.perform(post("/api/schedule/{list}", Date.valueOf("2022-04-02"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\"name\" : \"Breakfast\",\"client_id\" : \"1\",\"start_time\" : \"10:00\",\"finish_time\" : \"18:00\",\"day\" : \"2022-04-02\"}"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is("Breakfast")))
                    .andExpect(jsonPath("$.client_id", is("1")))
                    .andExpect(jsonPath("$.start_time", is("10:00:00")))
                    .andExpect(jsonPath("$.finish_time", is("18:00:00")))
                    .andExpect(jsonPath("$.day", is("2022-04-02")));
            ;
        }

    }
}
