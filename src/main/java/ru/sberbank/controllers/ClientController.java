package ru.sberbank.controllers;

import javafx.scene.chart.ScatterChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.entities.Client;
import ru.sberbank.exceptions.AlreadyHaveSuchEx;
import ru.sberbank.exceptions.NotFoundException;
import ru.sberbank.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private ClientService clientService;

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }

    @RequestMapping(value = "/clients/create", method = RequestMethod.POST)
    public ResponseEntity<?> createClient(@RequestBody Client client){
        try {
            clientService.save(client);
        } catch (AlreadyHaveSuchEx ex) {
            log.info("Client already exists!");
            ex.printStackTrace();;
        } 
     
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> showAllClients(){
        List<Client> clients = null;
        try {
            clients = clientService.getAllClientsList();
        } catch (NotFoundException ex) {
            log.info("Clients not found");
            ex.printStackTrace();
        }
        return clients != null && !clients.isEmpty()
                ? new ResponseEntity<>(clients, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/clients/{id}", method = RequestMethod.GET)
    public ResponseEntity<Client> showClientById(@PathVariable(name = "id") Long id){
        Client client = null;
        try {
        client = clientService.getClientById(id);
        } catch (NotFoundException ex) {
            log.info("Clinet by such id not found");
            ex.printStackTrace();
        }
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/clients/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
         clientService.delete(id);
         log.info("Client deleted");
         return new ResponseEntity<>(HttpStatus.OK);
    }



}
