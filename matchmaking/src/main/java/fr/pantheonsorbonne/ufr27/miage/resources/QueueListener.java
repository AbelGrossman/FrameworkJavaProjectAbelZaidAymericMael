package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QueueListener {
    @Inject
    QueueManager queueManager;

    @Incoming("queue-updates")
    public void handleQueueUpdate(String message) {
        // Deserialize message (JSON) to User object
        // Simulated here as a direct User creation
        User user = new User("playerId", "theme", 1500); // Example user
        queueManager.addPlayerToQueue(user);
    }
}
