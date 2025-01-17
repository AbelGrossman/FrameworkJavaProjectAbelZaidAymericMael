package fr.pantheonsorbonne.ufr27.miage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import fr.pantheonsorbonne.ufr27.miage.service.TeamEmitter;
import fr.pantheonsorbonne.ufr27.miage.dao.User;
import fr.pantheonsorbonne.ufr27.miage.model.Queue;

class QueueManagerTest {

    private QueueManager queueManager;
    private TeamEmitter mockTeamEmitter;

    @BeforeEach
    void setUp() {
        mockTeamEmitter = Mockito.mock(TeamEmitter.class);
        queueManager = new QueueManager(mockTeamEmitter);
    }

    @Test
    void testAddPlayerToQueue() {
        User user = new User("1", "Geography", 1000);
        queueManager.addPlayerToQueue(user);

        Queue queue = queueManager.getOrCreateQueue("Geography");
        assertEquals(1, queue.getPlayers().size());
        assertEquals(user, queue.getPlayers().get(0));
    }

    @Test
    void testFormTeams() {
        // Add players to the queue
        for (int i = 1; i <= 6; i++) {
            queueManager.addPlayerToQueue(new User(String.valueOf(i), "Geography", 1000 + i));
        }

        // Form teams
        queueManager.formTeams("Geography");

        // Verify the team emitter is called
        verify(mockTeamEmitter, times(1)).sendTeamToCreationPartie(anyList());
        Queue queue = queueManager.getOrCreateQueue("Geography");
        assertTrue(queue.getPlayers().isEmpty(), "Queue should be empty after forming a team.");
    }

    @Test
    void testAllowedMmrIncreasesPeriodically() throws InterruptedException {
    QueueManager realQueueManager = new QueueManager(mockTeamEmitter);

    // Add a queue with only 3 players (not enough for a team)
    for (int i = 1; i <= 3; i++) {
        realQueueManager.addPlayerToQueue(new User(String.valueOf(i), "Geography", 1000 + i));
    }

    Queue queue = realQueueManager.getOrCreateQueue("Geography");
    int initialAllowedMmr = queue.getAllowedMmrDifference();

    // Simulate periodic MMR adjustment
    realQueueManager.adjustMmrDifferencePeriodically(); // Simulate the first execution
    Thread.sleep(11000); // Simulate time passing for a second adjustment
    realQueueManager.adjustMmrDifferencePeriodically(); // Simulate the second execution

    assertTrue(queue.getAllowedMmrDifference() > initialAllowedMmr, 
               "Allowed MMR difference should increase after periodic adjustment.");
}

}
