package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dto.User;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("RankedUserProcessor")
public class RankedUserProcessor {

    @Inject
    QueueManager queueManager;
    
    public void processRankedUser(User user){
        queueManager.addPlayerToQueue(user);
    }

}
