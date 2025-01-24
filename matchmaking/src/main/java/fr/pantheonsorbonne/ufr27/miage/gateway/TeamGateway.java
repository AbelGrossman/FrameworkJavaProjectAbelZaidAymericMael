package fr.pantheonsorbonne.ufr27.miage.gateway;
import fr.pantheonsorbonne.ufr27.miage.model.Team;


import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TeamGateway {

    @Inject
    ProducerTemplate producerTemplate;

    public void sendTeamToCreationPartie(Team team) {
        producerTemplate.sendBody("direct:newTeam", team);
    }
}

