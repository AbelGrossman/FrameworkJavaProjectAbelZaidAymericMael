package fr.pantheonsorbonne.ufr27.miage.gateway;

import fr.pantheonsorbonne.ufr27.miage.service.QueueManagerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("CanceledMatchmakingGateway")
public class CanceledMatchmakingGateway {

    @Inject
    QueueManagerImpl queueManager;

    public void processCanceledMatchmaking(Long userId) {
        queueManager.removePlayerFromQueue(userId);
    }


    
}
