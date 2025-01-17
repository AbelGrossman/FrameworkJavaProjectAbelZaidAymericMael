package fr.pantheonsorbonne.ufr27.miage.service;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class TeamEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    public void sendTeamToCreationPartie(List<User> team) {
            producerTemplate.sendBody("direct:newTeam", team);
    }
}

@ApplicationScoped
public class UserEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    public void sendTeamToCreationPartie(List<User> team) {
            producerTemplate.sendBody("direct:zai", team);
    }
}
