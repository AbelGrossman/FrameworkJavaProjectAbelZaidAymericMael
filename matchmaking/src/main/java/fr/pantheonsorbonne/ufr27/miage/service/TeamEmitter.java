package fr.pantheonsorbonne.ufr27.miage.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class TeamEmitter {

    @Inject
    @Channel("team-updates")
    Emitter<String> teamEmitter;

    private static final Logger LOGGER = Logger.getLogger(TeamEmitter.class.getName());

    // ObjectMapper for JSON serialization
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendTeamToCreationPartie(List<User> team) {
        try {
            // Serialize the team into JSON
            String teamData = objectMapper.writeValueAsString(team);

            // Emit the serialized data to the topic
            teamEmitter.send(teamData);
            LOGGER.info("Team successfully sent to creation-partie: " + teamData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send team to creation-partie: " + e.getMessage(), e);
        }
    }
}
