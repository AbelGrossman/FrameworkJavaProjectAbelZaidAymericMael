package fr.pantheonsorbonne.ufr27.miage.service;

import org.apache.camel.ProducerTemplate;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import jakarta.inject.Inject;

public class UserEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    public void processRankedUser(User user){
        producerTemplate.sendBody("direct:userMmrRequest", user);
    }
    
}
