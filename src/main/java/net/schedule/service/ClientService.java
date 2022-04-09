package net.schedule.service;


import net.schedule.dto.ClientDto;
import net.schedule.model.Client;
import java.util.List;


public interface ClientService {

    Client getById(Long id);

    ClientDto save(ClientDto client);

    void delete(Long id);

    List<Client> getAll();

}
