package fr.pantheonsorbonne.ufr27.miage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;


import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;
import fr.pantheonsorbonne.ufr27.miage.dao.TeamDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.gateway.TeamGateway;
import fr.pantheonsorbonne.ufr27.miage.model.Queue;

class QueueManagerTest {

    private QueueManager queueManager;
    private TeamDAO mockTeamDAO;

    @BeforeEach
    void setUp() {
        mockTeamDAO = Mockito.mock(TeamDAO.class);
        queueManager = new QueueManager(mockTeamDAO);
    }

    @Test
    void testAddPlayerToQueue() {
        UserWithMmr user = new UserWithMmr(1L, "Geography", 1000);
        queueManager.addPlayerToQueue(user);

        Queue queue = queueManager.getOrCreateQueue("Geography");
        assertEquals(1, queue.getPlayers().size());
        assertEquals(user, queue.getPlayers().get(0));
    }

    @Test
    void testFormTeams() {
        // Add players to the queue
        for (Long i = 1L; i <= 6; i++) {
            int mmr = (int) (1000 + i);
            UserWithMmr user = new UserWithMmr(i, "Geography", mmr);
            queueManager.addPlayerToQueue(user);
        }

        // Form teams
        queueManager.formTeams("Geography");
        Queue queue = queueManager.getOrCreateQueue("Geography");
        assertTrue(queue.getPlayers().isEmpty(), "Queue should be empty after forming a team.");
    }

    @Test
    void testAllowedMmrIncreasesPeriodically() throws InterruptedException {
    QueueManager realQueueManager = new QueueManager(mockTeamDAO);

    // Add a queue with only 3 players (not enough for a team)
    for (Long i = 1L; i <= 3; i++) {
        int mmr = (int) (1000 + i);
        UserWithMmr user = new UserWithMmr(i, "Geography", mmr);
        realQueueManager.addPlayerToQueue(user);
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
