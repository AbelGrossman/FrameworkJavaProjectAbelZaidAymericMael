package fr.pantheonsorbonne.ufr27.miage.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.ShutdownEvent;

@ApplicationScoped
public class ApplicationLifecycle {

    private final MatchmakingWorker matchmakingWorker;

    public ApplicationLifecycle(MatchmakingWorker matchmakingWorker) {
        this.matchmakingWorker = matchmakingWorker;
    }

    void onStart(@Observes StartupEvent ev) {
        matchmakingWorker.processQueues();
        matchmakingWorker.adjustMmr();
    }
    
}

