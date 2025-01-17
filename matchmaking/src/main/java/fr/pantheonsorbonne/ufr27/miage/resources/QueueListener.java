package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QueueListener {

    private final QueueManager queueManager;

    public QueueListener(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Incoming("players") // Listening for events on the "players" channel
    public void onPlayerAddedToQueue(User user) {
        // Add the incoming player to the queue
        queueManager.addPlayerToQueue(user);
    }
}
