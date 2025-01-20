// package fr.pantheonsorbonne.ufr27.miage;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;

// import fr.pantheonsorbonne.ufr27.miage.model.User;
// import fr.pantheonsorbonne.ufr27.miage.service.QueueManager;

// import static org.mockito.Mockito.*;

// class QueueListenerTest {

//     private QueueManager mockQueueManager;

//     @BeforeEach
//     void setUp() {
//         mockQueueManager = Mockito.mock(QueueManager.class);
//     }

//     @Test
//     void testOnPlayerAddedToQueue() {
//         User user = new User("1", "Geography", 1000);
//         mockQueueManager.addPlayerToQueue(user);
//         verify(mockQueueManager, times(1)).addPlayerToQueue(user);
//     }
// }
