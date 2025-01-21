package fr.pantheonsorbonne.ufr27.miage.service;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;


@ApplicationScoped
public class MatchmakingWorker {

    private final QueueManager queueManager;

    public MatchmakingWorker(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Scheduled(every = "5s") // Runs every 5 seconds
    public void processQueues() {
        // Loop through all themes managed by the QueueManager
        for (String theme : queueManager.getThemes()) {
            queueManager.formTeams(theme); // Process each theme's queue to form teams
        }
    }

    @Scheduled(every = "5s") // Runs every  seconds
    public void adjustMmr() {
        // Loop through all themes managed by the QueueManager
            queueManager.adjustMmrDifferencePeriodically(); // Adjust each theme's players' MMR
    }
}