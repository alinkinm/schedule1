package net.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import net.schedule.dto.ClientDto;
import net.schedule.model.Client;
import net.schedule.repository.ClientRepository;
import net.schedule.repository.EventRepository;
import net.schedule.service.ClientService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final EventRepository eventRepository;

    @Override
    public Client getById(Long id) {
        return clientRepository.getById(id);
    }

    @Override
    public ClientDto save(ClientDto client) {
        return ClientDto.from(clientRepository.save(Client.builder()
                .name(client.getName())
                .login(client.getLogin())
                .build())
        );
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }


}
