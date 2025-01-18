package fr.pantheonsorbonne.ufr27.miage.service;
import fr.pantheonsorbonne.ufr27.miage.dto.Team;

import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TeamEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    public void sendTeamToCreationPartie(Team team) {
            producerTemplate.sendBody("direct:newTeam", team);
    }
}

