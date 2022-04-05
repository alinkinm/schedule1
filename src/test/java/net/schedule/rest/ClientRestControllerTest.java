package net.schedule.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.schedule.model.Client;
import net.schedule.model.Event;
import net.schedule.repository.EventRepository;
import net.schedule.service.ClientServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class ClientRestControllerTest {

    @InjectMocks
    private ClientRestController clientRestController;

    @Mock
    private RequestAttributes attributes;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ClientServiceImpl clientService;


    @Before
    public void setup() {
        RequestContextHolder.setRequestAttributes(attributes);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(clientRestController).build();

    }

    //надо ли отдельно тестировать статус, тело и медиатайп
    @Test
    void saveClientTesting() {
        ClientServiceImpl clientService = Mockito.mock(ClientServiceImpl.class);
        ClientRestController clientRestController = new ClientRestController(clientService);
        HttpHeaders headers = new HttpHeaders();
        Client client = new Client((long) 10, "chhhhhhh", "chhhhh");
        ResponseEntity<Client> responseEntity = clientRestController.saveClient(client);
        ResponseEntity<Client> responseEntity1 = new ResponseEntity<>(client, headers, HttpStatus.CREATED);
        assertEquals(responseEntity, responseEntity1);
        System.out.println(responseEntity.toString()+ responseEntity1);
       /* Client client = new Client("name", "login");
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Client> responseEntity = new ResponseEntity<>(client, headers, HttpStatus.CREATED);
        Mockito.when(client).thenReturn();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/userinfo/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());*/
    }

    //почему экспектедрезалт это стринг я не поняла
    @Test
    void saveEventTesting() {
        ClientServiceImpl clientService = Mockito.mock(ClientServiceImpl.class);
        ClientRestController clientRestController = new ClientRestController(clientService);
        HttpHeaders headers = new HttpHeaders();
        LocalTime start_time = LocalTime.of(10,0);
        LocalTime finish_time = LocalTime.of(15,0);
        Date day = Date.valueOf("2022-04-03");
        Event event = new Event("SomeEvent", (long)1, start_time, finish_time,
                day);
        ResponseEntity<Event> expectedResult = new ResponseEntity<>(event, headers, HttpStatus.CREATED);
        ResponseEntity<Event> realResult = clientRestController.saveEvent(event);
        assertEquals(expectedResult, realResult);
        System.out.println(expectedResult+ realResult.toString());

    }

    @Test
    void showScheduleTesting() {
        final Long client_id =10L;
        final Date day = Date.valueOf("2022-04-02");
    }

    @Test
    void showFreeTimeTesting() {
        //то же самое
    }

    @Test
    void showALlFreeTimeTesting() {

    }

    //а где проверять неполное заполнение данных

    @Test
    public void saveClientMediaTypeTesting() throws Exception {

    }

}