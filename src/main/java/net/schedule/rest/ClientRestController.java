package net.schedule.rest;


import lombok.RequiredArgsConstructor;
import net.schedule.dto.ClientDto;
import net.schedule.service.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/client")
public class ClientRestController {

    private final ClientServiceImpl clientService;

    @PostMapping
    public ResponseEntity<ClientDto> saveClient(@RequestBody @Validated ClientDto client) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.save(client));
    }





}
