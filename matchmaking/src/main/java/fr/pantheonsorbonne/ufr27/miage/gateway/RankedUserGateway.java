package fr.pantheonsorbonne.ufr27.miage.gateway;


import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("RankedUserGateway")
public class RankedUserGateway {

    @Inject
    QueueManager queueManager;
    
    public void processRankedUser(UserWithMmr rankedUser) {
        queueManager.addPlayerToQueue(rankedUser);
    }
}
