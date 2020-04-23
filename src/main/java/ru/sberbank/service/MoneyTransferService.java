package ru.sberbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import ru.sberbank.entities.Card;
import ru.sberbank.entities.Client;
import ru.sberbank.repositories.CardRepository;


import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Service
public class MoneyTransferService {
    private CardRepository cardRepository;

    private ClientService clientService;
    private CardService cardService;

    @Autowired
    public void setCardRepository(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }
    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
    @Autowired
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void addCard(Client client, Card cardFrom, Card cardTo){
        Set<Card> cards = new LinkedHashSet<>();
        client = clientService.getClientById(client.getClient_id());
        cardFrom = cardService.getCardById(cardFrom.getId());
        cardTo = cardService.getCardById(cardTo.getId());
        cards.add(cardFrom);
        cards.add(cardTo);
        cardTo.setClient(client);
        cardFrom.setClient(client);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void removeCard(Long cardId, Long clientId, List<Card> cards){
        Client client = clientService.getClientById(clientId);
        Card card = cardService.getCardById(cardId);
        cards.remove(card);
        card.setClient(client);
    }


    //TODO проверка наличия достаточных средств для перевода
    @Transactional(propagation = Propagation.REQUIRED)
    public void transfer(Long fromCardId, Long toCardId, int sumOfTransfer ){

           cardRepository.findById(fromCardId)
                   .map(c -> {c.setCash(c.getCash() - sumOfTransfer); return c;})
                   .map(cardRepository::save).orElseThrow(RuntimeException::new);

           cardRepository.findById(toCardId)
                   .map(c -> {c.setCash(c.getCash() + sumOfTransfer); return c;})
                   .map(cardRepository::save).orElseThrow(RuntimeException::new);
    }

}
