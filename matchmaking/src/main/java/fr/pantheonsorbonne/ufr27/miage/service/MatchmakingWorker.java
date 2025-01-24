package fr.pantheonsorbonne.ufr27.miage.service;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;


@ApplicationScoped
public class MatchmakingWorker {

    private final QueueManager queueManager;

    public MatchmakingWorker(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Scheduled(every = "5s")
    public void processQueues() {
        for (String theme : queueManager.getThemes()) {
            queueManager.formTeams(theme);
        }
    }

    @Scheduled(every = "5s") 
    public void adjustMmr() {
            queueManager.adjustMmrDifferencePeriodically(); 
    }
}