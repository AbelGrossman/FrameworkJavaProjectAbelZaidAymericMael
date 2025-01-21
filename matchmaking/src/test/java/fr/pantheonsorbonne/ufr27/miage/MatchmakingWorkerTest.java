// package fr.pantheonsorbonne.ufr27.miage;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;

// import fr.pantheonsorbonne.ufr27.miage.service.MatchmakingWorker;
// import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;

// import java.util.List;

// import static org.mockito.Mockito.*;

// class MatchmakingWorkerTest {

//     private MatchmakingWorker matchmakingWorker;
//     private QueueManager mockQueueManager;

//     @BeforeEach
//     void setUp() {
//         mockQueueManager = Mockito.mock(QueueManager.class);
//         matchmakingWorker = new MatchmakingWorker(mockQueueManager);
//     }

//     @Test
//     void testProcessQueues() {
//         when(mockQueueManager.getThemes()).thenReturn(List.of("Geography", "History"));

//         matchmakingWorker.processQueues();

//         // Verify that formTeams is called for each theme
//         verify(mockQueueManager, times(1)).formTeams("Geography");
//         verify(mockQueueManager, times(1)).formTeams("History");
//     }
// }
