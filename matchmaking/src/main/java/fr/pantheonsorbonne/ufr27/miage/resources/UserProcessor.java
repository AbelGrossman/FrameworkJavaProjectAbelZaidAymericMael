package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("UserProcessor")
public class UserProcessor {

    private final QueueManager queueManager;

    public UserProcessor(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public void processNewUser(User user) {
        // Add the incoming player to the queue
        queueManager.addPlayerToQueue(user);
    }
}
