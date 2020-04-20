package ru.sberbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sberbank.entities.Card;
import ru.sberbank.entities.Client;
import ru.sberbank.repositories.ClientRepository;
import ru.sberbank.repositories.CardRepository;

import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepository;
    private CardRepository cardRepository;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setCardRepository(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public Client getClientById(Long id){
        return clientRepository.findById(id).get();
    }

    public List<Client> getAllClientsList(){
        return clientRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    public Client save(Client client){
        Card card = (Card) cardRepository.findAll();
        Client cli = new Client();
        cli.setId(client.getId());
        cli.setFirstName(client.getFirstName());
        cli.setLastName(client.getLastName());
        cli.setLogin(client.getLogin());
        cli.setPassword(cli.getPassword());
        cli.setCards((List<Card>) card);
        return clientRepository.save(cli);
    }

    public void delete(Long id){
        clientRepository.deleteById(id);
    }








}
